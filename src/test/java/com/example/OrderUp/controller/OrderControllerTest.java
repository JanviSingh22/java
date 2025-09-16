//package com.example.OrderUp.controller;
//
//import com.example.OrderUp.model.Order;
//import com.example.OrderUp.model.OrderStatus;
//import com.example.OrderUp.model.Product;
//import com.example.OrderUp.service.OrderService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//// import the new annotation
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(OrderController.class)
//class OrderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private OrderService orderService;  // Replaces @MockBean
//
//    private Product product;
//    private Order order;
//
//    @BeforeEach
//    void setUp() {
//        product = new Product(1L, "Handmade Vase", 10, 100.0);
//        order = Order.builder()
//                .quantity(2)
//                .status(OrderStatus.CONFIRMED)
//                .product(product)
//                .build();
//    }
//
//    @Test
//    void placeOrder_Success() throws Exception {
//        when(orderService.placeOrder(1L, 2)).thenReturn(order);
//
//        mockMvc.perform(post("/orders/place")
//                        .param("productId", "1")
//                        .param("quantity", "2")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.quantity").value(2))
//                .andExpect(jsonPath("$.status").value("CONFIRMED"));
//
//        verify(orderService, times(1)).placeOrder(1L, 2);
//    }
//}
package com.example.OrderUp.controller;

import com.example.OrderUp.entity.Order;
import com.example.OrderUp.entity.OrderStatus;
import com.example.OrderUp.entity.Product;
import com.example.OrderUp.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean  // Proper way to inject a mock service
    private OrderService orderService;

    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Handmade Vase", 10, 100.0);
        order = Order.builder()
                .quantity(2)
                .status(OrderStatus.CONFIRMED)
                .product(product)
                .build();
    }

    @Test
    void placeOrder_Success() throws Exception {
        when(orderService.placeOrder(1L, 2)).thenReturn(order);

        mockMvc.perform(post("/orders/place")
                        .param("productId", "1")
                        .param("quantity", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(orderService, times(1)).placeOrder(1L, 2);
    }
}
