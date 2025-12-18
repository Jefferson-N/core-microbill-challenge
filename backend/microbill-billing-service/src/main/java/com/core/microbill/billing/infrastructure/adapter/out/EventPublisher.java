package com.core.microbill.billing.infrastructure.adapter.out;

import com.core.microbill.billing.domain.event.InvoiceCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.invoice}")
    private String invoiceExchange;

    @Value("${rabbitmq.routing-key.invoice-created}")
    private String invoiceCreatedRoutingKey;

    public void publishInvoiceCreated(InvoiceCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(invoiceExchange, invoiceCreatedRoutingKey, event);
            log.info("Published InvoiceCreated event for invoice: {}", event.getInvoiceId());
        } catch (Exception e) {
            log.error("Failed to publish InvoiceCreated event for invoice: {}", event.getInvoiceId(), e);
        }
    }
}