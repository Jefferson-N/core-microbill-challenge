package com.core.microbill.billing.infrastructure.adapter.in;

import com.core.microbill.billing.api.InvoicesApi;
import com.core.microbill.billing.api.model.*;
import com.core.microbill.billing.domain.model.Invoice;
import com.core.microbill.billing.domain.model.InvoiceItem;
import com.core.microbill.billing.domain.port.in.InvoiceInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class InvoiceController implements InvoicesApi {
    
    private final InvoiceInputPort invoiceInputPort;

    @Override
    public ResponseEntity<InvoiceResponse> createInvoice(InvoiceRequest request) {
        Invoice invoice = Invoice.builder()
                .customerId(request.getCustomerId())
                .providerId(request.getProviderId())
                .items(request.getItems().stream()
                        .map(this::toItemDomain)
                        .collect(Collectors.toList()))
                .build();
        
        Invoice created = invoiceInputPort.create(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    public ResponseEntity<InvoiceResponse> getInvoiceById(Long id) {
        Invoice invoice = invoiceInputPort.findById(id);
        return ResponseEntity.ok(toResponse(invoice));
    }

    @Override
    public ResponseEntity<InvoicePageResponse> getInvoices(Integer page, Integer size, Long customerId) {
        Page<Invoice> invoices = invoiceInputPort.findAll(
                PageRequest.of(page, size), customerId);
        
        InvoicePageResponse response = new InvoicePageResponse();
        response.setContent(invoices.getContent().stream().map(this::toResponse).toList());
        response.setTotalElements(invoices.getTotalElements());
        response.setTotalPages(invoices.getTotalPages());
        response.setSize(invoices.getSize());
        response.setNumber(invoices.getNumber());
        
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InvoiceResponse> updateInvoice(Long id, InvoiceRequest request) {
        Invoice invoice = Invoice.builder()
                .customerId(request.getCustomerId())
                .providerId(request.getProviderId())
                .items(request.getItems().stream()
                        .map(this::toItemDomain)
                        .collect(Collectors.toList()))
                .build();
        
        Invoice updated = invoiceInputPort.update(id, invoice);
        return ResponseEntity.ok(toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> deleteInvoice(Long id) {
        invoiceInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<byte[]> generateInvoiceReport(Long id) {
        byte[] pdf = invoiceInputPort.generateReport(id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }

    private InvoiceItem toItemDomain(InvoiceItemRequest request) {
        return InvoiceItem.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .taxRate(request.getTaxRate())
                .build();
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setId(invoice.getId());
        response.setCustomerId(invoice.getCustomerId());
        response.setProviderId(invoice.getProviderId());
        response.setIssueDate(invoice.getIssueDate());
        response.setSubtotal(invoice.getSubtotal());
        response.setTaxTotal(invoice.getTaxTotal());
        response.setTotal(invoice.getTotal());
        response.setStatus(invoice.getStatus());
        response.setItems(invoice.getItems().stream()
                .map(this::toItemResponse)
                .toList());
        return response;
    }

    private InvoiceItemResponse toItemResponse(InvoiceItem item) {
        InvoiceItemResponse response = new InvoiceItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProductId());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setTaxRate(item.getTaxRate());
        response.setLineTotal(item.getLineTotal());
        return response;
    }
}
