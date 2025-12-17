package com.core.microbill.management.infrastructure.adapter.out.persistence;

import com.core.microbill.management.domain.model.Provider;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProviderMapper {
    Provider toDomain(ProviderEntity entity);
    ProviderEntity toEntity(Provider domain);
}
