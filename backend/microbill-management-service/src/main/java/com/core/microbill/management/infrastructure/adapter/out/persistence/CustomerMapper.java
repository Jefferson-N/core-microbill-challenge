package com.core.microbill.management.infrastructure.adapter.out.persistence;

import com.core.microbill.management.domain.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toDomain(CustomerEntity entity);
    CustomerEntity toEntity(Customer domain);
}
