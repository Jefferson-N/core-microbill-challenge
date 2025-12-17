package com.core.microbill.management.infrastructure.adapter.in;

import com.core.microbill.management.api.CustomersApi;
import com.core.microbill.management.api.model.CustomerPageResponse;
import com.core.microbill.management.api.model.CustomerRequest;
import com.core.microbill.management.api.model.CustomerResponse;
import com.core.microbill.management.domain.model.Customer;
import com.core.microbill.management.domain.port.in.CustomerInputPort;
import com.core.microbill.management.infrastructure.mapper.CustomerRequestMapper;
import com.core.microbill.management.infrastructure.mapper.CustomerResponseMapper;
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
    private final CustomerRequestMapper customerRequestMapper;
    private final CustomerResponseMapper customerResponseMapper;

    @Override
    public ResponseEntity<CustomerResponse> createCustomer(CustomerRequest request) {
        Customer customer = customerRequestMapper.toDomain(request);
        Customer created = customerInputPort.create(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponseMapper.toResponse(created));
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomerById(Long id) {
        Customer customer = customerInputPort.findById(id);
        return ResponseEntity.ok(customerResponseMapper.toResponse(customer));
    }

    @Override
    public ResponseEntity<CustomerPageResponse> getCustomers(Integer page, Integer size, String search) {
        Page<Customer> customers = customerInputPort.findAll(PageRequest.of(page, size), search);

        CustomerPageResponse response = new CustomerPageResponse();
        response.setContent(customerResponseMapper.toResponseList(customers.getContent()));
        response.setTotalElements(customers.getTotalElements());
        response.setTotalPages(customers.getTotalPages());
        response.setSize(customers.getSize());
        response.setNumber(customers.getNumber());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRequestMapper.toDomain(request);
        Customer updated = customerInputPort.update(id, customer);
        return ResponseEntity.ok(customerResponseMapper.toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(Long id) {
        customerInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
