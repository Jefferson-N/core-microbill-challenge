package com.core.microbill.management.domain.port.in;

import com.core.microbill.management.domain.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderInputPort {
    Provider create(Provider provider);
    Provider findById(Long id);
    Page<Provider> findAll(Pageable pageable, String search);
    Provider update(Long id, Provider provider);
    void delete(Long id);
}
