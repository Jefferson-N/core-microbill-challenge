package com.core.microbill.billing.infrastructure.adapter.in;

import com.core.microbill.billing.domain.event.InvoiceCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.invoice-created}")
    public void handleInvoiceCreated(InvoiceCreatedEvent event) {
        log.info("Processing InvoiceCreated event for invoice: {}", event.getInvoiceId());
        
        try {
            generatePdfAsync(event);
            sendNotification(event);
            
            log.info("Successfully processed InvoiceCreated event for invoice: {}", event.getInvoiceId());
        } catch (Exception e) {
            log.error("Failed to process InvoiceCreated event for invoice: {}", event.getInvoiceId(), e);
        }
    }

    private void generatePdfAsync(InvoiceCreatedEvent event) {
        log.info("Generating PDF asynchronously for invoice: {}", event.getInvoiceId());
    }

    private void sendNotification(InvoiceCreatedEvent event) {
        log.info("Sending notification for invoice: {} to customer: {}", 
                event.getInvoiceId(), event.getCustomerId());
    }
}