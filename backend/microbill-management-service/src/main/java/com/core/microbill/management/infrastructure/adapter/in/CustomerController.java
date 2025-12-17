package com.core.microbill.management.infrastructure.adapter.in;

import com.core.microbill.management.api.CustomersApi;
import com.core.microbill.management.api.model.*;
import com.core.microbill.management.domain.model.Customer;
import com.core.microbill.management.domain.port.in.CustomerInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomersApi {
    
    private final CustomerInputPort customerInputPort;

    @Override
    public ResponseEntity<CustomerResponse> createCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .docNumber(request.getDocNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();
        
        Customer created = customerInputPort.create(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomerById(Long id) {
        Customer customer = customerInputPort.findById(id);
        return ResponseEntity.ok(toResponse(customer));
    }

    @Override
    public ResponseEntity<CustomerPageResponse> getCustomers(Integer page, Integer size, String search) {
        Page<Customer> customers = customerInputPort.findAll(
                PageRequest.of(page, size), search);
        
        CustomerPageResponse response = new CustomerPageResponse();
        response.setContent(customers.getContent().stream().map(this::toResponse).toList());
        response.setTotalElements(customers.getTotalElements());
        response.setTotalPages(customers.getTotalPages());
        response.setSize(customers.getSize());
        response.setNumber(customers.getNumber());
        
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(Long id, CustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .docNumber(request.getDocNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();
        
        Customer updated = customerInputPort.update(id, customer);
        return ResponseEntity.ok(toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(Long id) {
        customerInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setDocNumber(customer.getDocNumber());
        response.setEmail(customer.getEmail());
        response.setAddress(customer.getAddress());
        return response;
    }
}
