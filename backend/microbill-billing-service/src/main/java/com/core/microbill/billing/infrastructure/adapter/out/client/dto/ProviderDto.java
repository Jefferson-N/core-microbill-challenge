package com.core.microbill.billing.infrastructure.adapter.out.client.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProviderDto {
    private Long id;
    private String name;
    private String taxId;
    private String email;
    private String address;
}