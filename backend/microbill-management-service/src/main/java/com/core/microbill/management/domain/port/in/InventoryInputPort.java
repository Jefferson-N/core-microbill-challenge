package com.core.microbill.management.domain.port.in;

public interface InventoryInputPort { void decreaseStock(Long productId, Integer quantity); }