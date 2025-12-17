package com.core.microbill.billing.infrastructure.adapter.out.messaging;

import com.core.microbill.billing.domain.model.Invoice;
import com.core.microbill.billing.domain.port.out.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQEventPublisher implements EventPublisherPort {
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${rabbitmq.exchange.invoice}")
    private String exchange;
    
    @Value("${rabbitmq.routing-key.invoice-created}")
    private String routingKey;

    @Override
    public void publishInvoiceCreated(Invoice invoice) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, invoice);
            log.info("Published InvoiceCreated event for invoice: {}", invoice.getId());
        } catch (Exception e) {
            log.error("Error publishing InvoiceCreated event", e);
        }
    }
}
