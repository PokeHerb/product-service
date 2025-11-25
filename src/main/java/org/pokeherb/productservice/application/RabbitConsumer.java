package org.pokeherb.productservice.application;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class RabbitConsumer {

    @RabbitListener(queues = "product")
    public void handleMessage(Message message) {

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        switch (routingKey) {
            case "product.delivery.allocated":
                System.out.println("전달받은 이벤트: " + payload);
                // 이벤트 로직 처리
                break;
        }

    }

}
