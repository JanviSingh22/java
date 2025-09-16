package com.example.OrderUp.integration;

import com.example.OrderUp.dto.OrderRequest;
import com.example.OrderUp.entity.Product;
import com.example.OrderUp.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        product = new Product();
        product.setName("Handmade Mug");
        product.setStock(10);
        product.setPrice(50.0);

        productRepository.save(product);
    }

    @Test
    void placeOrder_Success() throws Exception {
        OrderRequest request = new OrderRequest(product.getId(), 5);

        mockMvc.perform(post("/orders/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void placeOrder_OutOfStock() throws Exception {
        OrderRequest request = new OrderRequest(product.getId(), 20);

        mockMvc.perform(post("/orders/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Not enough stock for product: Handmade Mug"));
    }

    @Test
    void placeOrder_ProductNotFound() throws Exception {
        OrderRequest request = new OrderRequest(999L, 2);

        mockMvc.perform(post("/orders/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found with id: 999"));
    }
}
