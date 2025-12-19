package com.core.microbill.management.domain.port.in.events;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Map;

public interface StockUpdateConsumerOutPort {
    @RabbitListener(queues = "${rabbitmq.queue.invoice-created}")
    void handleInvoiceCreated(Map<String, Object> event);
}
