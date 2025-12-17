package com.core.microbill.management.application.service;

import com.core.microbill.management.domain.model.Customer;
import com.core.microbill.management.domain.port.in.CustomerInputPort;
import com.core.microbill.management.domain.port.out.CustomerOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService implements CustomerInputPort {
    
    private final CustomerOutputPort customerOutputPort;

    @Override
    public Customer create(Customer customer) {
        if (customerOutputPort.existsByDocNumber(customer.getDocNumber())) {
            throw new RuntimeException("Customer with doc number already exists");
        }
        return customerOutputPort.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerOutputPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> findAll(Pageable pageable, String search) {
        return customerOutputPort.findAll(pageable, search);
    }

    @Override
    public Customer update(Long id, Customer customer) {
        Customer existing = findById(id);
        existing.setName(customer.getName());
        existing.setEmail(customer.getEmail());
        existing.setAddress(customer.getAddress());
        return customerOutputPort.save(existing);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        customerOutputPort.deleteById(id);
    }
}
