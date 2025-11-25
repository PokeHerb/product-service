package org.pokeherb.productservice.infrastructure.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.pokeherb.productservice.global.infrastructure.exception.CustomException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class RabbitProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void publishDeliveryEvent(Object payLoad, String routingKey) {
        sendMessage("pokeherb", routingKey, payLoad);

    }

    private void sendMessage(String exchange, String routingKey, Object payload) {

        try {
            String json = objectMapper.writeValueAsString(payload);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(json.getBytes(StandardCharsets.UTF_8), messageProperties);

            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (JsonProcessingException e) {
            throw new CustomException(RabbitErrorCode.RABBITMQ_CONNECTION_FAILED);
        }
    }
}
