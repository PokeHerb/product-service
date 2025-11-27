package org.pokeherb.productservice.infrastructure.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitProductProperties.class)
public class RabbitConfig {

    private final RabbitProductProperties productProperties;
    private final static String DLX = "product.dlx";
    private final static String DLQ = "product.dlq";

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 리스너 컨테이너 설정
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        // 예외 발생 시 다시 같은 큐가 아닌, DLXf로 보내도록 false
        factory.setDefaultRequeueRejected(false);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;

    }

//    @Bean
//    public TopicExchange productExchange() {
//        return new TopicExchange(productProperties.exchange(), true, false);
//    }

    @Bean
    public TopicExchange productExchange() {
        return ExchangeBuilder
                .topicExchange(productProperties.exchange())
                .durable(true)
                .build();
    }

    @Bean
    public Queue productQueue() {
        return QueueBuilder
                .durable(productProperties.queue())
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    public Queue productDeadLetterQueue() {
        return QueueBuilder
                .durable(DLQ)
                .build();
    }

    @Bean
    public Binding productDecreaseStockBinding(Queue productQueue, TopicExchange productExchange) {

        return BindingBuilder
                .bind(productQueue)
                .to(productExchange)
                .with(productProperties.routingKeyDecrease());
    }

    @Bean
    public Binding productIncreaseStockBinding(Queue productQueue, TopicExchange productExchange) {

        return BindingBuilder
                .bind(productQueue)
                .to(productExchange)
                .with(productProperties.routingKeyIncrease());
    }

    @Bean
    public Binding productDeadLetterBinding(Queue productDeadLetterQueue, TopicExchange productExchange) {
        return BindingBuilder
                .bind(productDeadLetterQueue)
                .to(productExchange)
                .with(DLQ);
    }
}