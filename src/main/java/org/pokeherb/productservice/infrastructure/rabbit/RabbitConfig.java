package org.pokeherb.productservice.infrastructure.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitProducer.class)
public class RabbitConfig {

    private final RabbitProducer productProperties;
}
