package com.core.microbill.billing.infrastructure.adapter.in;

import com.core.microbill.billing.api.InvoicesApi;
import com.core.microbill.billing.api.model.InvoiceRequest;
import com.core.microbill.billing.api.model.InvoiceResponse;
import com.core.microbill.billing.api.model.InvoicePageResponse;
import com.core.microbill.billing.domain.model.Invoice;
import com.core.microbill.billing.domain.port.in.InvoiceInputPort;
import com.core.microbill.billing.infrastructure.adapter.mapper.InvoiceResponseMapper;
import com.core.microbill.billing.infrastructure.adapter.mapper.InvoiceRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class InvoiceController implements InvoicesApi {

    private final InvoiceInputPort invoiceInputPort;
    private final InvoiceResponseMapper invoiceResponseMapper;
    private final InvoiceRequestMapper invoiceRequestMapper;

    @Override
    public ResponseEntity<InvoiceResponse> createInvoice(InvoiceRequest request) {
        InvoiceResponse invoiceResponse = Optional.ofNullable(request)
                .map(invoiceRequestMapper::toDomain)
                .map(invoiceInputPort::create)
                .map(invoiceResponseMapper::toResponse)
                .orElse(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceResponse);
    }

    @Override
    public ResponseEntity<InvoiceResponse> getInvoiceById(Long id) {
        Invoice invoice = invoiceInputPort.findById(id);
        return ResponseEntity.ok(invoiceResponseMapper.toResponse(invoice));
    }

    @Override
    public ResponseEntity<InvoicePageResponse> getInvoices(Integer page, Integer size, Long customerId) {
        Page<Invoice> invoices = invoiceInputPort.findAll(PageRequest.of(page, size), customerId);

        InvoicePageResponse response = new InvoicePageResponse();
        response.setContent(invoiceResponseMapper.toResponseList(invoices.getContent()));
        response.setTotalElements(invoices.getTotalElements());
        response.setTotalPages(invoices.getTotalPages());
        response.setSize(invoices.getSize());
        response.setNumber(invoices.getNumber());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InvoiceResponse> updateInvoice(Long id, InvoiceRequest request) {
        Invoice invoice = invoiceRequestMapper.toDomain(request);
        Invoice updated = invoiceInputPort.update(id, invoice);
        return ResponseEntity.ok(invoiceResponseMapper.toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> deleteInvoice(Long id) {
        invoiceInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Resource> generateInvoiceReport(Long id) {
        byte[] pdf = invoiceInputPort.generateReport(id);

        Resource resource = new ByteArrayResource(pdf);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=invoice-" + id + ".pdf")
                .body(resource);
    }

}
