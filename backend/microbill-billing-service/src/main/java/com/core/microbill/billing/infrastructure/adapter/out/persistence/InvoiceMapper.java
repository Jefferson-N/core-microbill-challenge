package com.core.microbill.billing.infrastructure.adapter.out.persistence;

import com.core.microbill.billing.domain.model.Invoice;
import com.core.microbill.billing.domain.model.InvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    
    @Mapping(target = "items", ignore = true)
    Invoice toDomain(InvoiceEntity entity);
    
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InvoiceEntity toEntity(Invoice domain);
    
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    InvoiceItemEntity toItemEntity(InvoiceItem item);
    
    InvoiceItem toItemDomain(InvoiceItemEntity entity);
}
