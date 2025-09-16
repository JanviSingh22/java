package com.example.OrderUp.service;

import com.example.OrderUp.dto.OrderRequest;
import com.example.OrderUp.dto.OrderResponse;
import com.example.OrderUp.entity.Order;
import com.example.OrderUp.entity.OrderStatus;
import com.example.OrderUp.entity.Product;
import com.example.OrderUp.exception.OutOfStockException;
import com.example.OrderUp.exception.ProductNotFoundException;
import com.example.OrderUp.repository.OrderRepository;
import com.example.OrderUp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final Lock lock = new ReentrantLock(); // ensures thread-safe stock update

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    // Old method (optional, can remove later)
    public Order placeOrder(Long productId, int quantity) {
        lock.lock();
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

            if (product.getStock() < quantity) {
                throw new OutOfStockException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            Order order = Order.builder()
                    .quantity(quantity)
                    .status(OrderStatus.CONFIRMED)
                    .product(product)
                    .build();

            return orderRepository.save(order);
        } finally {
            lock.unlock();
        }
    }

    // New method using DTOs
    public OrderResponse placeOrder(OrderRequest request) {
        lock.lock();
        try {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(
                            "Product not found with id: " + request.getProductId()));

            if (product.getStock() < request.getQuantity()) {
                throw new OutOfStockException(
                        "Not enough stock for product: " + product.getName());
            }

            // Deduct stock and save
            product.setStock(product.getStock() - request.getQuantity());
            productRepository.save(product);

            // Create order
            Order order = Order.builder()
                    .product(product)
                    .quantity(request.getQuantity())
                    .status(OrderStatus.CONFIRMED)
                    .build();

            Order savedOrder = orderRepository.save(order);

            // Map to DTO
            return new OrderResponse(
                    savedOrder.getId(),
                    savedOrder.getQuantity(),
                    savedOrder.getStatus(),
                    product.getId(),
                    product.getName()
            );
        } finally {
            lock.unlock();
        }
    }
}
