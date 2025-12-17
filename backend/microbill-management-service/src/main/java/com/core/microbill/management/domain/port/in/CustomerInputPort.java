package com.core.microbill.management.domain.port.in;

import com.core.microbill.management.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerInputPort {
    Customer create(Customer customer);
    Customer findById(Long id);
    Page<Customer> findAll(Pageable pageable, String search);
    Customer update(Long id, Customer customer);
    void delete(Long id);
}
