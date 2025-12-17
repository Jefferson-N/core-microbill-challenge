package com.core.microbill.management.infrastructure.mapper;

import com.core.microbill.management.domain.model.Product;
import com.core.microbill.management.api.model.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ProductResponseMapper {

    @Mapping(source = "price", target = "price", qualifiedByName = "bigDecimalToDouble")
    @Mapping(source = "taxRate", target = "taxRate", qualifiedByName = "bigDecimalToDouble")
    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        return Optional.ofNullable(value)
                .map(BigDecimal::doubleValue)
                .orElse(0.0);
    }
}
