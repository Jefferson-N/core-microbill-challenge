package com.core.microbill.billing.application.service;

import com.core.microbill.billing.domain.exception.BusinessLogicException;
import com.core.microbill.billing.domain.exception.ResourceNotFoundException;
import com.core.microbill.billing.domain.exception.ValidationException;
import com.core.microbill.billing.domain.model.Invoice;
import com.core.microbill.billing.domain.model.InvoiceItem;
import com.core.microbill.billing.domain.port.in.InvoiceInputPort;
import com.core.microbill.billing.domain.port.out.EventPublisherPort;
import com.core.microbill.billing.domain.port.out.InvoiceOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService implements InvoiceInputPort {
    
    private final InvoiceOutputPort invoiceOutputPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public Invoice create(Invoice invoice) {
        if (invoice.getCustomerId() == null || invoice.getCustomerId() <= 0) {
            throw new ValidationException("El ID del cliente es requerido y debe ser positivo");
        }
        if (invoice.getProviderId() == null || invoice.getProviderId() <= 0) {
            throw new ValidationException("El ID del proveedor es requerido y debe ser positivo");
        }
        if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
            throw new ValidationException("La factura debe tener al menos un item");
        }
        
        calculateTotals(invoice);
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setStatus("CREATED");
        
        Invoice saved = invoiceOutputPort.save(invoice);
        eventPublisherPort.publishInvoiceCreated(saved);
        
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Invoice findById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("El ID de la factura debe ser un número positivo");
        }
        return invoiceOutputPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura con ID " + id + " no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> findAll(Pageable pageable, Long customerId) {
        return invoiceOutputPort.findAll(pageable, customerId);
    }

    @Override
    public Invoice update(Long id, Invoice invoice) {
        Invoice existing = findById(id);
        existing.setCustomerId(invoice.getCustomerId());
        existing.setProviderId(invoice.getProviderId());
        existing.setItems(invoice.getItems());
        calculateTotals(existing);
        return invoiceOutputPort.save(existing);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        invoiceOutputPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateReport(Long id) {
        Invoice invoice = findById(id);
        return new byte[0];
    }

    private void calculateTotals(Invoice invoice) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;

        for (InvoiceItem item : invoice.getItems()) {
            if (item.getQuantity() <= 0) {
                throw new ValidationException("La cantidad del item debe ser positiva");
            }
            if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException("El precio unitario debe ser no negativo");
            }
            
            BigDecimal itemSubtotal = item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            BigDecimal itemTax = itemSubtotal.multiply(item.getTaxRate());
            BigDecimal itemTotal = itemSubtotal.add(itemTax);
            
            item.setLineTotal(itemTotal);
            subtotal = subtotal.add(itemSubtotal);
            taxTotal = taxTotal.add(itemTax);
        }

        invoice.setSubtotal(subtotal);
        invoice.setTaxTotal(taxTotal);
        invoice.setTotal(subtotal.add(taxTotal));
    }
}
