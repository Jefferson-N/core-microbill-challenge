package com.core.microbill.management.application.service;

import com.core.microbill.management.domain.exception.BusinessLogicException;
import com.core.microbill.management.domain.exception.ResourceNotFoundException;
import com.core.microbill.management.domain.exception.ValidationException;
import com.core.microbill.management.domain.model.Provider;
import com.core.microbill.management.domain.port.in.ProviderInputPort;
import com.core.microbill.management.domain.port.out.ProviderOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ProviderService implements ProviderInputPort {
    
    private final ProviderOutputPort providerOutputPort;

    @Override
    public Provider create(Provider provider) {
        if (!StringUtils.hasText(provider.getName())) {
            throw new ValidationException("El nombre del proveedor es requerido");
        }
        if (!StringUtils.hasText(provider.getTaxId())) {
            throw new ValidationException("El RUC/NIF del proveedor es requerido");
        }
        if (providerOutputPort.existsByTaxId(provider.getTaxId())) {
            throw new BusinessLogicException("Ya existe un proveedor con el RUC/NIF " + provider.getTaxId());
        }
        return providerOutputPort.save(provider);
    }

    @Override
    @Transactional(readOnly = true)
    public Provider findById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("El ID del proveedor debe ser un número positivo");
        }
        return providerOutputPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor con ID " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Provider> findAll(Pageable pageable, String search) {
        return providerOutputPort.findAll(pageable, search);
    }

    @Override
    public Provider update(Long id, Provider provider) {
        if (!StringUtils.hasText(provider.getName())) {
            throw new ValidationException("El nombre del proveedor es requerido");
        }
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
