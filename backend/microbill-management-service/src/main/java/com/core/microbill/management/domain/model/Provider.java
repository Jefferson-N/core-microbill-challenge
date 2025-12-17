package com.core.microbill.management.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Provider {
    private Long id;
    private String name;
    private String taxId;
    private String email;
    private String address;
}
