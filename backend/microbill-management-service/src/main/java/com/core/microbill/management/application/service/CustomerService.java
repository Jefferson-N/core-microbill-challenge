package com.core.microbill.management.application.service;

import com.core.microbill.management.domain.model.Customer;
import com.core.microbill.management.domain.port.in.CustomerUseCase;
import com.core.microbill.management.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService implements CustomerUseCase {
    
    private final CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer) {
        if (customerRepository.existsByDocNumber(customer.getDocNumber())) {
            throw new RuntimeException("Customer with doc number already exists");
        }
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> findAll(Pageable pageable, String search) {
        return customerRepository.findAll(pageable, search);
    }

    @Override
    public Customer update(Long id, Customer customer) {
        Customer existing = findById(id);
        existing.setName(customer.getName());
        existing.setEmail(customer.getEmail());
        existing.setAddress(customer.getAddress());
        return customerRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        customerRepository.deleteById(id);
    }
}
