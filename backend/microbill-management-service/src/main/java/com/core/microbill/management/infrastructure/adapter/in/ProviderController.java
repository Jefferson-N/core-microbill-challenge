package com.core.microbill.management.infrastructure.adapter.in;

import com.core.microbill.management.api.ProvidersApi;
import com.core.microbill.management.api.model.*;
import com.core.microbill.management.domain.model.Provider;
import com.core.microbill.management.domain.port.in.ProviderInputPort;
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

    @Override
    public ResponseEntity<ProviderResponse> createProvider(ProviderRequest request) {
        Provider provider = Provider.builder()
                .name(request.getName())
                .taxId(request.getTaxId())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();
        
        Provider created = providerInputPort.create(provider);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    public ResponseEntity<ProviderResponse> getProviderById(Long id) {
        Provider provider = providerInputPort.findById(id);
        return ResponseEntity.ok(toResponse(provider));
    }

    @Override
    public ResponseEntity<ProviderPageResponse> getProviders(Integer page, Integer size, String search) {
        Page<Provider> providers = providerInputPort.findAll(
                PageRequest.of(page, size), search);
        
        ProviderPageResponse response = new ProviderPageResponse();
        response.setContent(providers.getContent().stream().map(this::toResponse).toList());
        response.setTotalElements(providers.getTotalElements());
        response.setTotalPages(providers.getTotalPages());
        response.setSize(providers.getSize());
        response.setNumber(providers.getNumber());
        
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProviderResponse> updateProvider(Long id, ProviderRequest request) {
        Provider provider = Provider.builder()
                .name(request.getName())
                .taxId(request.getTaxId())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();
        
        Provider updated = providerInputPort.update(id, provider);
        return ResponseEntity.ok(toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> deleteProvider(Long id) {
        providerInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ProviderResponse toResponse(Provider provider) {
        ProviderResponse response = new ProviderResponse();
        response.setId(provider.getId());
        response.setName(provider.getName());
        response.setTaxId(provider.getTaxId());
        response.setEmail(provider.getEmail());
        response.setAddress(provider.getAddress());
        return response;
    }
}
