package com.example.week4.finaltask.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

import com.example.week4.finaltask.dto.ProductRequest;
import com.example.week4.finaltask.dto.ProductResponse;
import com.example.week4.finaltask.entity.Product;
import com.example.week4.finaltask.exception.ProductNotFoundException;
import com.example.week4.finaltask.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        return toResponse(productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id)));
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = toEntity(request);
        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        Product updatedProduct = productRepository.save(product);
        return toResponse(updatedProduct);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }
    
    private Product toEntity(ProductRequest request) {
        return new Product(
                null,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock()
        );
    }
}
