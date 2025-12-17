package com.core.microbill.management.domain.port.in;

import com.core.microbill.management.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductInputPort {
    Product create(Product product);
    Product findById(Long id);
    Page<Product> findAll(Pageable pageable, String search);
    Product update(Long id, Product product);
    void delete(Long id);
}
