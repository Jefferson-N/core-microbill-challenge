package com.core.microbill.management.infrastructure.mapper;

import com.core.microbill.management.domain.model.Provider;
import com.core.microbill.management.infrastructure.adapter.out.entities.ProviderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProviderMapper {
    Provider toDomain(ProviderEntity entity);
    ProviderEntity toEntity(Provider domain);
}
