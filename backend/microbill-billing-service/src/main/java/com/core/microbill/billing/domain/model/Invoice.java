package com.core.microbill.billing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private Long id;
    private Long customerId;
    private Long providerId;
    private LocalDateTime issueDate;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private String status;
    private List<InvoiceItem> items;
}
