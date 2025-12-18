/*
package com.core.microbill.billing.infrastructure.adapter.out;

import com.core.microbill.billing.domain.event.InvoiceCreatedEvent;
import com.core.microbill.billing.domain.model.Invoice;
import com.core.microbill.billing.domain.port.out.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventPublisherAdapter implements EventPublisherPort {

    private final EventPublisher eventPublisher;

    @Override
    public void publishInvoiceCreated(Invoice invoice) {
        InvoiceCreatedEvent event = InvoiceCreatedEvent.builder()
                .invoiceId(invoice.getId())
                .customerId(invoice.getCustomerId())
                .providerId(invoice.getProviderId())
                .total(invoice.getTotal())
                .items(invoice.getItems().stream()
                        .map(item -> InvoiceCreatedEvent.InvoiceItemEvent.builder()
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .lineTotal(item.getLineTotal())
                                .build())
                        .collect(Collectors.toList()))
                .createdAt(LocalDateTime.now())
                .build();

        eventPublisher.publishInvoiceCreated(event);
    }
}*/
