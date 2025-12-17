package com.core.microbill.management.application.service;

import com.core.microbill.management.domain.model.Product;
import com.core.microbill.management.domain.port.in.ProductInputPort;
import com.core.microbill.management.domain.port.out.ProductOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService implements ProductInputPort {
    
    private final ProductOutputPort productOutputPort;

    @Override
    public Product create(Product product) {
        if (productOutputPort.existsByCode(product.getCode())) {
            throw new RuntimeException("Product with code already exists");
        }
        return productOutputPort.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productOutputPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable, String search) {
        return productOutputPort.findAll(pageable, search);
    }

    @Override
    public Product update(Long id, Product product) {
        Product existing = findById(id);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setTaxRate(product.getTaxRate());
        existing.setStock(product.getStock());
        return productOutputPort.save(existing);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        productOutputPort.deleteById(id);
    }
}
