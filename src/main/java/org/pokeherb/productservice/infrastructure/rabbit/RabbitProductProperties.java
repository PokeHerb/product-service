package org.pokeherb.productservice.infrastructure.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbit.product")
public record RabbitProductProperties(
        String exchange,
        String queue,
        String routingKey
) {

}
