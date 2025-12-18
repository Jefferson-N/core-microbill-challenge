package com.core.microbill.billing.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.invoice}")
    private String invoiceExchange;

    @Value("${rabbitmq.queue.invoice-created}")
    private String invoiceCreatedQueue;

    @Value("${rabbitmq.routing-key.invoice-created}")
    private String invoiceCreatedRoutingKey;

    @Bean
    public TopicExchange invoiceExchange() {
        return new TopicExchange(invoiceExchange);
    }

    @Bean
    public Queue invoiceCreatedQueue() {
        return new Queue(invoiceCreatedQueue, true);
    }

    @Bean
    public Binding invoiceCreatedBinding() {
        return BindingBuilder
                .bind(invoiceCreatedQueue())
                .to(invoiceExchange())
                .with(invoiceCreatedRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}