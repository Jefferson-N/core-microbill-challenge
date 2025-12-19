package com.core.microbill.management.application.event;

import com.core.microbill.management.domain.exception.EventException;
import com.core.microbill.management.domain.port.in.InventoryInputPort;
import com.core.microbill.management.domain.port.out.ProductOutputPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class InventoryService implements InventoryInputPort {
    private final ProductOutputPort productOutputPort;

    @Override
    public void decreaseStock(Long productId, Integer quantity) {
        var product = productOutputPort.findById(productId).orElseThrow(() -> new EventException("Producto no encontrado"));
        int newStock = product.getStock() - quantity;
        if (newStock < 0) {
            throw new EventException("Stock insuficiente para productId=" + productId);
        }
        product.setStock(newStock);
        productOutputPort.save(product);
    }
}