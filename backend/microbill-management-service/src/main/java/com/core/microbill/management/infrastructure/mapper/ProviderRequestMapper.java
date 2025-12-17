package com.core.microbill.management.infrastructure.mapper;

import com.core.microbill.management.domain.model.Provider;
import com.core.microbill.management.api.model.ProviderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ProviderRequestMapper {

    @Mapping(source = "name", target = "name", qualifiedByName = "stringOrEmpty")
    @Mapping(source = "taxId", target = "taxId", qualifiedByName = "stringOrEmpty")
    @Mapping(source = "email", target = "email", qualifiedByName = "stringOrEmpty")
    @Mapping(source = "address", target = "address", qualifiedByName = "stringOrEmpty")
    Provider toDomain(ProviderRequest request);

    List<Provider> toDomainList(List<ProviderRequest> requests);

    @Named("stringOrEmpty")
    default String stringOrEmpty(String value) {
        return Optional.ofNullable(value).orElse("");
    }
}
