package com.core.microbill.billing.domain.port.out;

import com.core.microbill.billing.domain.model.Invoice;

public interface EventPublisherPort {
    void publishInvoiceCreated(Invoice invoice);
}