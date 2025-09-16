package com.example.OrderUp.service;

import com.example.OrderUp.dto.ProductRequest;
import com.example.OrderUp.dto.ProductResponse;
import com.example.OrderUp.entity.Product;
import com.example.OrderUp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create a new product
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    // Get all products
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Helper method to convert entity to DTO
    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getStock(),
                product.getPrice()
        );
    }
}
