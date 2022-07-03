package kz.cake.web.model;

import kz.cake.web.entity.Product;

public class OrderDetailDto {
    private Long id;
    private boolean active;
    private Long orderId;
    private int quantity = 0;
    private Product product;

    public OrderDetailDto() {
    }

    public OrderDetailDto(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "OrderDetailDto{" +
                "id=" + id +
                ", active=" + active +
                ", orderId=" + orderId +
                ", quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}
