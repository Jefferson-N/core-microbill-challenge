package com.core.microbill.management.infrastructure.adapter.out;

import com.core.microbill.management.domain.model.Provider;
import com.core.microbill.management.domain.port.out.ProviderOutputPort;
import com.core.microbill.management.infrastructure.adapter.out.entities.ProviderEntity;
import com.core.microbill.management.infrastructure.adapter.out.persistence.ProviderJpaRepository;
import com.core.microbill.management.infrastructure.mapper.ProviderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProviderRepositoryAdapter implements ProviderOutputPort {
    
    private final ProviderJpaRepository jpaRepository;
    private final ProviderMapper mapper;

    @Override
    public Provider save(Provider provider) {
        ProviderEntity entity = mapper.toEntity(provider);
        ProviderEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Provider> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Provider> findAll(Pageable pageable, String search) {
        return jpaRepository.findAllWithSearch(search, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByTaxId(String taxId) {
        return jpaRepository.existsByTaxId(taxId);
    }
}
