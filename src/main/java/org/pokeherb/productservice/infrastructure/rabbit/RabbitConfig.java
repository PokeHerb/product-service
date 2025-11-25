package org.pokeherb.productservice.infrastructure.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitProductProperties.class)
public class RabbitConfig {

    private final RabbitProductProperties productProperties;

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(productProperties.exchange(), true, false);
    }

    @Bean
    public Queue productQueue() {
        return QueueBuilder.durable(productProperties.queue()).build();
    }

    @Bean
    public Binding productBinding(Queue hubQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(hubQueue).to(productExchange)
                .with(productProperties.routingKey());
    }
}