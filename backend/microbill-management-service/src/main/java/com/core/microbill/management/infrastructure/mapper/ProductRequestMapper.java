package com.core.microbill.management.infrastructure.mapper;

import com.core.microbill.management.domain.model.Product;
import com.core.microbill.management.api.model.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ProductRequestMapper {

    @Mapping(source = "price", target = "price", qualifiedByName = "doubleToBigDecimal")
    @Mapping(source = "taxRate", target = "taxRate", qualifiedByName = "doubleToBigDecimal")
    Product toDomain(ProductRequest request);

    List<Product> toDomainList(List<ProductRequest> requests);

    @Mapping(source = "price", target = "price", qualifiedByName = "bigDecimalToDouble")
    @Mapping(source = "taxRate", target = "taxRate", qualifiedByName = "bigDecimalToDouble")
    ProductRequest toRequest(Product product);

    List<ProductRequest> toRequestList(List<Product> products);

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double value) {
        return Optional.ofNullable(value)
                .map(BigDecimal::valueOf)
                .orElse(BigDecimal.ZERO);
    }

    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        return Optional.ofNullable(value)
                .map(BigDecimal::doubleValue)
                .orElse(0.0);
    }
}
