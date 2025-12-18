package com.core.microbill.billing.infrastructure.adapter.out.client;

import com.core.microbill.billing.infrastructure.adapter.out.client.dto.CustomerDto;
import com.core.microbill.billing.infrastructure.adapter.out.client.dto.ProviderDto;
import com.core.microbill.billing.infrastructure.adapter.out.client.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ManagementServiceClient {

    private final RestTemplate restTemplate;

    @Value("${management.service.url:http://management-service:8082}")
    private String managementServiceUrl;

    private HttpHeaders buildAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getCredentials() instanceof String token) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    public CustomerDto getCustomer(Long customerId) {
        try {
            String url = managementServiceUrl + "/api/customers/" + customerId;
            HttpEntity<Void> entity = new HttpEntity<>(buildAuthHeaders());
            ResponseEntity<CustomerDto> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, CustomerDto.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching customer with ID: {}", customerId, e);
            return null;
        }
    }

    public ProviderDto getProvider(Long providerId) {
        try {
            String url = managementServiceUrl + "/api/providers/" + providerId;
            HttpEntity<Void> entity = new HttpEntity<>(buildAuthHeaders());
            ResponseEntity<ProviderDto> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, ProviderDto.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching provider with ID: {}", providerId, e);
            return null;
        }
    }

    public ProductDto getProduct(Long productId) {
        try {
            String url = managementServiceUrl + "/api/products/" + productId;
            HttpEntity<Void> entity = new HttpEntity<>(buildAuthHeaders());
            ResponseEntity<ProductDto> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, ProductDto.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching product with ID: {}", productId, e);
            return null;
        }
    }
}
