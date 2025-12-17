package com.core.microbill.management.infrastructure.adapter.in;

import com.core.microbill.management.api.ProductsApi;
import com.core.microbill.management.api.model.*;
import com.core.microbill.management.domain.model.Product;
import com.core.microbill.management.domain.port.in.ProductInputPort;
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

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductRequest request) {
        Product product = Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .price(request.getPrice())
                .taxRate(request.getTaxRate())
                .stock(request.getStock())
                .build();
        
        Product created = productInputPort.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(Long id) {
        Product product = productInputPort.findById(id);
        return ResponseEntity.ok(toResponse(product));
    }

    @Override
    public ResponseEntity<ProductPageResponse> getProducts(Integer page, Integer size, String search) {
        Page<Product> products = productInputPort.findAll(
                PageRequest.of(page, size), search);
        
        ProductPageResponse response = new ProductPageResponse();
        response.setContent(products.getContent().stream().map(this::toResponse).toList());
        response.setTotalElements(products.getTotalElements());
        response.setTotalPages(products.getTotalPages());
        response.setSize(products.getSize());
        response.setNumber(products.getNumber());
        
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(Long id, ProductRequest request) {
        Product product = Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .price(request.getPrice())
                .taxRate(request.getTaxRate())
                .stock(request.getStock())
                .build();
        
        Product updated = productInputPort.update(id, product);
        return ResponseEntity.ok(toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        productInputPort.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setCode(product.getCode());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setTaxRate(product.getTaxRate());
        response.setStock(product.getStock());
        return response;
    }
}
