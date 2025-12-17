package com.core.microbill.billing.domain.port.out;

import com.core.microbill.billing.domain.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InvoiceOutputPort {
    Invoice save(Invoice invoice);
    Optional<Invoice> findById(Long id);
    Page<Invoice> findAll(Pageable pageable, Long customerId);
    void deleteById(Long id);
}
