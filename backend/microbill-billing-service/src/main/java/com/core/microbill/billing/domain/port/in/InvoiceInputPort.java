package com.core.microbill.billing.domain.port.in;

import com.core.microbill.billing.domain.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvoiceInputPort {
    Invoice create(Invoice invoice);
    Invoice findById(Long id);
    Page<Invoice> findAll(Pageable pageable, Long customerId);
    Invoice update(Long id, Invoice invoice);
    void delete(Long id);
    byte[] generateReport(Long id);
}
