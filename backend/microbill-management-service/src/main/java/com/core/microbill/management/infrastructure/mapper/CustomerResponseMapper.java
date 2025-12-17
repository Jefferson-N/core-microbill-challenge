package com.core.microbill.management.infrastructure.mapper;

import com.core.microbill.management.domain.model.Customer;
import com.core.microbill.management.api.model.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CustomerResponseMapper {

    @Mapping(source = "id", target = "id", qualifiedByName = "longOrZero")
    @Mapping(source = "name", target = "name", qualifiedByName = "stringOrEmpty")
    @Mapping(source = "docNumber", target = "docNumber", qualifiedByName = "stringOrEmpty")
    @Mapping(source = "email", target = "email", qualifiedByName = "stringOrEmpty")
    @Mapping(source = "address", target = "address", qualifiedByName = "stringOrEmpty")
    CustomerResponse toResponse(Customer customer);

    List<CustomerResponse> toResponseList(List<Customer> customers);

    @Named("longOrZero")
    default Long longOrZero(Long value) {
        return Optional.ofNullable(value).orElse(0L);
    }

    @Named("stringOrEmpty")
    default String stringOrEmpty(String value) {
        return Optional.ofNullable(value).orElse("");
    }
}
