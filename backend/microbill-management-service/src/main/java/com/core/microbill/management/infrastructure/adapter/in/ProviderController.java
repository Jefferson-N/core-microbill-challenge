package com.core.microbill.management.infrastructure.adapter.in;

import com.core.microbill.management.api.ProvidersApi;
import com.core.microbill.management.api.model.ProviderPageResponse;
import com.core.microbill.management.api.model.ProviderRequest;
import com.core.microbill.management.api.model.ProviderResponse;
import com.core.microbill.management.domain.model.Provider;
import com.core.microbill.management.domain.port.in.ProviderInputPort;
import com.core.microbill.management.infrastructure.mapper.ProviderRequestMapper;
import com.core.microbill.management.infrastructure.mapper.ProviderResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProviderController implements ProvidersApi {

    private final ProviderInputPort providerInputPort;
    private final ProviderRequestMapper providerRequestMapper;
    private final ProviderResponseMapper providerResponseMapper;

    @Override
    public ResponseEntity<ProviderResponse> createProvider(ProviderRequest request) {
        Provider provider = providerRequestMapper.toDomain(request);
        Provider created = providerInputPort.create(provider);
        return ResponseEntity.status(HttpStatus.CREATED).body(providerResponseMapper.toResponse(created));
    }

    @Override
    public ResponseEntity<ProviderResponse> getProviderById(Long id) {
        Provider provider = providerInputPort.findById(id);
        return ResponseEntity.ok(providerResponseMapper.toResponse(provider));
    }

    @Override
    public ResponseEntity<ProviderPageResponse> getProviders(Integer page, Integer size, String search) {
        Page<Provider> providers = providerInputPort.findAll(PageRequest.of(page, size), search);

        ProviderPageResponse response = new ProviderPageResponse();
        response.setContent(providerResponseMapper.toResponseList(providers.getContent()));
        response.setTotalElements(providers.getTotalElements());
        response.setTotalPages(providers.getTotalPages());
        response.setSize(providers.getSize());
        response.setNumber(providers.getNumber());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProviderResponse> updateProvider(Long id, ProviderRequest request) {
        Provider provider = providerRequestMapper.toDomain(request);
        Provider updated = providerInputPort.update(id, provider);
        return ResponseEntity.ok(providerResponseMapper.toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> deleteProvider(Long id) {
        providerInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
