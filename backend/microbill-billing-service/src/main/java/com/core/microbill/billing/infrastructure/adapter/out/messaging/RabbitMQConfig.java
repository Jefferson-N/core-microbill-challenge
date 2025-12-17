package com.core.microbill.billing.infrastructure.adapter.out.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    @Value("${rabbitmq.exchange.invoice}")
    private String exchange;
    
    @Value("${rabbitmq.queue.invoice-created}")
    private String queue;
    
    @Value("${rabbitmq.routing-key.invoice-created}")
    private String routingKey;

    @Bean
    public TopicExchange invoiceExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue invoiceCreatedQueue() {
        return new Queue(queue, true);
    }

    @Bean
    public Binding invoiceCreatedBinding() {
        return BindingBuilder
                .bind(invoiceCreatedQueue())
                .to(invoiceExchange())
                .with(routingKey);
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
