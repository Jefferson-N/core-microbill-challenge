package com.core.microbill.management.domain.port.out;

import com.core.microbill.management.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerOutputPort {
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    Page<Customer> findAll(Pageable pageable, String search);
    void deleteById(Long id);
    boolean existsByDocNumber(String docNumber);
}
