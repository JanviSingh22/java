package com.example.OrderUp.service;

import com.example.OrderUp.exception.OutOfStockException;
import com.example.OrderUp.exception.ProductNotFoundException;
import com.example.OrderUp.entity.Order;
import com.example.OrderUp.entity.OrderStatus;
import com.example.OrderUp.entity.Product;
import com.example.OrderUp.repository.OrderRepository;
import com.example.OrderUp.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product(1L, "Handmade Mug", 10, 50.0);
    }

    @Test
    void placeOrder_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order order = orderService.placeOrder(1L, 5);

        // Assert
        assertNotNull(order);
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(5, order.getQuantity());
        assertEquals(5, product.getStock()); // stock reduced

        verify(productRepository, times(1)).save(product);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void placeOrder_ProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.placeOrder(999L, 2));

        verify(productRepository, times(1)).findById(999L);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_OutOfStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(OutOfStockException.class, () -> orderService.placeOrder(1L, 20));

        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any());
    }
}
