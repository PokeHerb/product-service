package org.pokeherb.productservice.infrastructure.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.productservice.global.infrastructure.exception.CustomException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final RabbitProductProperties rabbitProductProperties;

    public void publishDeliveryEvent(Object payLoad, String routingKey) {

        sendMessage(rabbitProductProperties.exchange(), routingKey, payLoad);

    }

    private void sendMessage(String exchange, String routingKey, Object payload) {

        try {

            log.info("RabbitMQ 메시지 발행 시작 - Exchange: {}, RoutingKey: {}, Payload: {}", exchange, routingKey, payload);

            String json = objectMapper.writeValueAsString(payload);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(json.getBytes(StandardCharsets.UTF_8), messageProperties);

            rabbitTemplate.convertAndSend(exchange, routingKey, message);

            log.info("RabbitMQ 메시지 발행 완료 - Exchange: {}, RoutingKey: {}, Payload: {}", exchange, routingKey, payload);

        } catch (JsonProcessingException e) {
            throw new CustomException(RabbitErrorCode.RABBITMQ_PROCESSING_ERROR);
        } catch (AmqpException e) {
            log.error("RabbitMQ 연결 또는 전송 중 오류 발생", e);
            throw new CustomException(RabbitErrorCode.RABBITMQ_CONNECTION_FAILED);
        }
    }
}
