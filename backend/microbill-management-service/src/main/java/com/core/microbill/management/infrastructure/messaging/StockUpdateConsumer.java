package com.core.microbill.management.infrastructure.messaging;

import com.core.microbill.management.domain.event.InvoiceCreatedEvent;
import com.core.microbill.management.domain.port.in.InventoryInputPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@AllArgsConstructor
@Slf4j
public class StockUpdateConsumer {

    private final  InventoryInputPort inventoryInputPort;


    @RabbitListener(queues = "${rabbitmq.queue.invoice-created}")
    public void handleInvoiceCreated(InvoiceCreatedEvent event) {

        log.info("Procesando invoice {}", event.getInvoiceId());
        event.getItems().forEach(item -> inventoryInputPort.decreaseStock(item.getProductId(), item.getQuantity()));
    }
}