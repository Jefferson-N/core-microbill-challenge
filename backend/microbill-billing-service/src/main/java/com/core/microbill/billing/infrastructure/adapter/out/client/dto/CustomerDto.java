package com.core.microbill.billing.infrastructure.adapter.out.client.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerDto {
    private Long id;
    private String name;
    private String docNumber;
    private String email;
    private String address;
}