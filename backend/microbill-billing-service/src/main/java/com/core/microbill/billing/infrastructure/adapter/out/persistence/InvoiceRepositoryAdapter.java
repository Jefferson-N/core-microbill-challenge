package com.core.microbill.billing.infrastructure.adapter.out.persistence;

import com.core.microbill.billing.domain.model.Invoice;
import com.core.microbill.billing.domain.model.InvoiceItem;
import com.core.microbill.billing.domain.port.out.InvoiceOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoiceRepositoryAdapter implements InvoiceOutputPort {
    
    private final InvoiceJpaRepository jpaRepository;
    private final InvoiceMapper mapper;

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = mapper.toEntity(invoice);
        
        if (invoice.getItems() != null) {
            entity.getItems().clear();
            for (InvoiceItem item : invoice.getItems()) {
                InvoiceItemEntity itemEntity = mapper.toItemEntity(item);
                entity.addItem(itemEntity);
            }
        }
        
        InvoiceEntity saved = jpaRepository.save(entity);
        return toDomainWithItems(saved);
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomainWithItems);
    }

    @Override
    public Page<Invoice> findAll(Pageable pageable, Long customerId) {
        return jpaRepository.findAllByCustomerId(customerId, pageable)
                .map(this::toDomainWithItems);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private Invoice toDomainWithItems(InvoiceEntity entity) {
        Invoice invoice = mapper.toDomain(entity);
        invoice.setItems(entity.getItems().stream()
                .map(mapper::toItemDomain)
                .collect(Collectors.toList()));
        return invoice;
    }
}
