package com.core.microbill.management.infrastructure.adapter.in;

import com.core.microbill.management.api.ProductsApi;
import com.core.microbill.management.api.model.ProductPageResponse;
import com.core.microbill.management.api.model.ProductRequest;
import com.core.microbill.management.api.model.ProductResponse;
import com.core.microbill.management.domain.model.Product;
import com.core.microbill.management.domain.port.in.ProductInputPort;
import com.core.microbill.management.infrastructure.mapper.ProductRequestMapper;
import com.core.microbill.management.infrastructure.mapper.ProductResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {

    private final ProductInputPort productInputPort;
    private final ProductRequestMapper productRequestMapper;
    private final ProductResponseMapper productResponseMapper;

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductRequest request) {
        Product product = productRequestMapper.toDomain(request);
        Product created = productInputPort.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseMapper.toResponse(created));
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(Long id) {
        Product product = productInputPort.findById(id);
        return ResponseEntity.ok(productResponseMapper.toResponse(product));
    }

    @Override
    public ResponseEntity<ProductPageResponse> getProducts(Integer page, Integer size, String search) {
        Page<Product> products = productInputPort.findAll(PageRequest.of(page, size), search);

        ProductPageResponse response = new ProductPageResponse();
        response.setContent(productResponseMapper.toResponseList(products.getContent()));
        response.setTotalElements(products.getTotalElements());
        response.setTotalPages(products.getTotalPages());
        response.setSize(products.getSize());
        response.setNumber(products.getNumber());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(Long id, ProductRequest request) {
        Product product = productRequestMapper.toDomain(request);
        Product updated = productInputPort.update(id, product);
        return ResponseEntity.ok(productResponseMapper.toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        productInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
