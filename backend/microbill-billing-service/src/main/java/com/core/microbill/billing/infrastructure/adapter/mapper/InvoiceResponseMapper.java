package com.core.microbill.billing.infrastructure.adapter.mapper;

import com.core.microbill.billing.api.model.InvoiceResponse;
import com.core.microbill.billing.domain.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceResponseMapper {

    @Mapping(source = "subtotal", target = "subtotal", qualifiedByName = "bigDecimalToDouble")
    @Mapping(source = "taxTotal", target = "taxTotal", qualifiedByName = "bigDecimalToDouble")
    @Mapping(source = "total", target = "total", qualifiedByName = "bigDecimalToDouble")
    InvoiceResponse toResponse(Invoice invoice);

    List<InvoiceResponse> toResponseList(List<Invoice> invoices);

    @Mapping(source = "subtotal", target = "subtotal", qualifiedByName = "doubleToBigDecimal")
    @Mapping(source = "taxTotal", target = "taxTotal", qualifiedByName = "doubleToBigDecimal")
    @Mapping(source = "total", target = "total", qualifiedByName = "doubleToBigDecimal")
    Invoice toDomain(InvoiceResponse response);

    List<Invoice> toDomainList(List<InvoiceResponse> responses);

    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double value) {
        return Optional.of(value)
                .map(BigDecimal::valueOf)
                .orElseGet(() -> BigDecimal.valueOf(0));
    }

    default OffsetDateTime map(LocalDateTime value) {
        return value != null ? value.atOffset(OffsetDateTime.now().getOffset()) : null;
    }

    default LocalDateTime map(OffsetDateTime value) {
        return value != null ? value.toLocalDateTime() : null;
    }
}
