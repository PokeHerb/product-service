package org.pokeherb.productservice.infrastructure.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.productservice.application.service.ProductStockService;
import org.pokeherb.productservice.infrastructure.rabbit.dto.OrderCancelledEventDto;
import org.pokeherb.productservice.infrastructure.rabbit.dto.OrderCreatedEventDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitConsumer {

    private final ProductStockService productStockService;
    private final ObjectMapper objectMapper;

/*    @RabbitListener(queues = "product")
    public void handleMessage(Message message) {

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        switch (routingKey) {
            case "product.decrease.stock":
                System.out.println("전달받은 이벤트: " + payload);
                handleOrderCreated(payload);
                // 이벤트 로직 처리
                break;
        }

    }*/

    /*@RabbitListener(queues = "#{rabbitProductProperties.queue()}")*/
    @RabbitListener(queues = "product")
    public void handleOrderEvent(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        {

            String payload = new String(message.getBody(), StandardCharsets.UTF_8);
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            log.info("전달받은 이벤트: {}", payload);

            switch (routingKey) {
                case "product.decrease.stock" -> {
                    OrderCreatedEventDto event = objectMapper.readValue(payload, OrderCreatedEventDto.class);
                    handleOrderCreated(event);
                    break;
                }
                case "product.increase.stock" -> {
                    OrderCancelledEventDto event = objectMapper.readValue(payload, OrderCancelledEventDto.class);
                    handleOrderCancelled(event);
                    break;
                }
            }
        }
    }


    private void handleOrderCreated(OrderCreatedEventDto event) {
        log.info("OrderCreatedEvent 수신 -> productId={}, quantity={}", event.productId(), event.quantity());

        try {
            productStockService.decreaseStock(event.productId(), event.quantity());

            log.info("재고 감소 처리 완료 -> productId={}, quantity={}", event.productId(), event.quantity());

        } catch (Exception e) {

            log.error("재고 감소 처리 실패 -> productId={}, quantity={}", event.productId(), event.quantity(), e);
            throw e;
        }
    }

    private void handleOrderCancelled(OrderCancelledEventDto event) {
        log.info("OrderCancelledEvent 수신 -> productId={}, quantity={}", event.productId(), event.quantity());

        try {
            productStockService.increaseStock(event.productId(), event.quantity());

            log.info("재고 증가 처리 완료 -> productId={}, quantity={}", event.productId(), event.quantity());

        } catch (Exception e) {

            log.error("재고 증가 처리 실패 -> productId={}, quantity={}", event.productId(), event.quantity(), e);
            throw e;
        }
    }
}


