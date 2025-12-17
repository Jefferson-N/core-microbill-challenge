package com.core.microbill.management.domain.port.out;

import com.core.microbill.management.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductOutputPort {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Page<Product> findAll(Pageable pageable, String search);
    void deleteById(Long id);
    boolean existsByCode(String code);
}
