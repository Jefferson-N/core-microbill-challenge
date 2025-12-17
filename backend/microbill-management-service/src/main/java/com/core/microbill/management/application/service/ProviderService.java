package com.core.microbill.management.application.service;

import com.core.microbill.management.domain.model.Provider;
import com.core.microbill.management.domain.port.in.ProviderInputPort;
import com.core.microbill.management.domain.port.out.ProviderOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProviderService implements ProviderInputPort {
    
    private final ProviderOutputPort providerOutputPort;

    @Override
    public Provider create(Provider provider) {
        if (providerOutputPort.existsByTaxId(provider.getTaxId())) {
            throw new RuntimeException("Provider with tax ID already exists");
        }
        return providerOutputPort.save(provider);
    }

    @Override
    @Transactional(readOnly = true)
    public Provider findById(Long id) {
        return providerOutputPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Provider> findAll(Pageable pageable, String search) {
        return providerOutputPort.findAll(pageable, search);
    }

    @Override
    public Provider update(Long id, Provider provider) {
        Provider existing = findById(id);
        existing.setName(provider.getName());
        existing.setEmail(provider.getEmail());
        existing.setAddress(provider.getAddress());
        return providerOutputPort.save(existing);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        providerOutputPort.deleteById(id);
    }
}
