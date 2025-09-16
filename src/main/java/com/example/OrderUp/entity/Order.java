package com.example.OrderUp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")  // avoid reserved keyword 'order'
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Default constructor (required by JPA)
    protected Order() {}

    public Order(Long id, int quantity, OrderStatus status, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.status = status;
        this.product = product;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    // ✅ Builder Pattern
    public static class OrderBuilder {
        private int quantity;
        private OrderStatus status;
        private Product product;

        public OrderBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderBuilder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public OrderBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public Order build() {
            return new Order(null, quantity, status, product);
        }
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }
}
