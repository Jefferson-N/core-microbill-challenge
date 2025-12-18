package com.core.microbill.management.application.service;

import com.core.microbill.management.domain.exception.BusinessLogicException;
import com.core.microbill.management.domain.exception.ResourceNotFoundException;
import com.core.microbill.management.domain.exception.ValidationException;
import com.core.microbill.management.domain.model.Customer;
import com.core.microbill.management.domain.port.in.CustomerInputPort;
import com.core.microbill.management.domain.port.out.CustomerOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService implements CustomerInputPort {
    
    private final CustomerOutputPort customerOutputPort;

    @Override
    public Customer create(Customer customer) {
        if (!StringUtils.hasText(customer.getName())) {
            throw new ValidationException("El nombre del cliente es requerido");
        }
        if (!StringUtils.hasText(customer.getDocNumber())) {
            throw new ValidationException("El número de documento es requerido");
        }
        if (customerOutputPort.existsByDocNumber(customer.getDocNumber())) {
            throw new BusinessLogicException("Ya existe un cliente con el número de documento "
                    + customer.getDocNumber());
        }
        return customerOutputPort.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("El ID del cliente debe ser un número positivo");
        }
        return customerOutputPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> findAll(Pageable pageable, String search) {
        return customerOutputPort.findAll(pageable, search);
    }

    @Override
    public Customer update(Long id, Customer customer) {
        if (!StringUtils.hasText(customer.getName())) {
            throw new ValidationException("El nombre del cliente es requerido");
        }
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
