package com.core.microbill.management.domain.port.out;

import com.core.microbill.management.domain.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProviderOutputPort {
    Provider save(Provider provider);
    Optional<Provider> findById(Long id);
    Page<Provider> findAll(Pageable pageable, String search);
    void deleteById(Long id);
    boolean existsByTaxId(String taxId);
}
