package com.core.microbill.billing.infrastructure.adapter.out.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceItemData {
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double lineTotal;
}