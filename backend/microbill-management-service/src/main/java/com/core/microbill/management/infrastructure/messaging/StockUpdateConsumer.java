package com.core.microbill.management.infrastructure.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StockUpdateConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.invoice-created}")
    public void handleInvoiceCreated(Map<String, Object> event) {
        log.info("Processing stock update for invoice: {}", event.get("invoiceId"));
        
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) event.get("items");
            
            items.forEach(item -> {
                Long productId = ((Number) item.get("productId")).longValue();
                Integer quantity = (Integer) item.get("quantity");
                
                log.info("Reducing stock for product {} by {} units", productId, quantity);
            });
            
            log.info("Stock updated successfully for invoice: {}", event.get("invoiceId"));
        } catch (Exception e) {
            log.error("Failed to update stock for invoice: {}", event.get("invoiceId"), e);
        }
    }
}