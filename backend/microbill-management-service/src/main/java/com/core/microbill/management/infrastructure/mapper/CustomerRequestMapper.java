package com.core.microbill.management.infrastructure.mapper;

import com.core.microbill.management.domain.model.Customer;
import com.core.microbill.management.api.model.CustomerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CustomerRequestMapper {

    @Mapping(source = "name", target = "name", qualifiedByName = "stringOrEmpty")
    @Mapping(source = "docNumber", target = "docNumber", qualifiedByName = "stringOrEmpty")
    @Mapping(source = "email", target = "email", qualifiedByName = "stringOrEmpty")
    @Mapping(source = "address", target = "address", qualifiedByName = "stringOrEmpty")
    Customer toDomain(CustomerRequest request);

    List<Customer> toDomainList(List<CustomerRequest> requests);

    @Named("stringOrEmpty")
    default String stringOrEmpty(String value) {
        return Optional.ofNullable(value).orElse("");
    }
}
