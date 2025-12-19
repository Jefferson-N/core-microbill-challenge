package com.core.microbill.management.application.service;

import com.core.microbill.management.domain.exception.BusinessLogicException;
import com.core.microbill.management.domain.exception.ResourceNotFoundException;
import com.core.microbill.management.domain.exception.ValidationException;
import com.core.microbill.management.domain.model.Product;
import com.core.microbill.management.domain.port.in.ProductInputPort;
import com.core.microbill.management.domain.port.out.ProductOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService implements ProductInputPort {
    
    private final ProductOutputPort productOutputPort;

    @Override
    public Product create(Product product) {
        if (!StringUtils.hasText(product.getCode())) {
            throw new ValidationException("El código del producto es requerido");
        }
        if (!StringUtils.hasText(product.getName())) {
            throw new ValidationException("El nombre del producto es requerido");
        }
        if (product.getPrice() == null || product.getPrice().doubleValue() < 0) {
            throw new ValidationException("El precio del producto debe ser no negativo");
        }
        if (productOutputPort.existsByCode(product.getCode())) {
            throw new BusinessLogicException("Ya existe un producto con el código " + product.getCode());
        }
        return productOutputPort.save(product);
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("El ID del producto debe ser un número positivo");
        }
        return productOutputPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + id + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable, String search) {
        return productOutputPort.findAll(pageable, search);
    }

    @Override
    public Product update(Long id, Product product) {
        if (!StringUtils.hasText(product.getName())) {
            throw new ValidationException("El nombre del producto es requerido");
        }
        if (product.getPrice() == null || product.getPrice().doubleValue() < 0) {
            throw new ValidationException("El precio del producto debe ser no negativo");
        }
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
