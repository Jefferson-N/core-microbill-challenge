package com.core.microbill.billing.infrastructure.adapter.mapper;

import com.core.microbill.billing.api.model.InvoiceRequest;
import com.core.microbill.billing.api.model.InvoiceItemRequest;
import com.core.microbill.billing.domain.model.Invoice;
import com.core.microbill.billing.domain.model.InvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceRequestMapper {

    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "providerId", target = "providerId")
    @Mapping(source = "items", target = "items")
    Invoice toDomain(InvoiceRequest request);

    List<Invoice> toDomainList(List<InvoiceRequest> requests);

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unitPrice", target = "unitPrice", qualifiedByName = "doubleToBigDecimal")
    @Mapping(source = "taxRate", target = "taxRate", qualifiedByName = "doubleToBigDecimal")
    InvoiceItem toItemDomain(InvoiceItemRequest request);

    List<InvoiceItem> toItemDomainList(List<InvoiceItemRequest> requests);

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }
}
