package com.core.microbill.management.infrastructure.mapper;

import com.core.microbill.management.domain.model.Product;
import com.core.microbill.management.infrastructure.adapter.out.entities.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toDomain(ProductEntity entity);
    ProductEntity toEntity(Product domain);
}
