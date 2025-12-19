package com.core.microbill.management.infrastructure.mapper;

import com.core.microbill.management.domain.model.Customer;
import com.core.microbill.management.infrastructure.adapter.out.entities.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toDomain(CustomerEntity entity);
    CustomerEntity toEntity(Customer domain);
}
