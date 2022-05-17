package kz.cake.web.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order extends Base<Long> {
    private Long userId;
    private Long orderDetailId;
    private Long orderStatusId;
    private LocalDateTime orderDate;
    private LocalDateTime shippingDate;
    private BigDecimal total;

    private Order(Long userId, Long orderDetailId, Long orderStatusId, LocalDateTime orderDate, LocalDateTime shippingDate, BigDecimal total) {
        this.userId = userId;
        this.orderDetailId = orderDetailId;
        this.orderStatusId = orderStatusId;
        this.orderDate = orderDate;
        this.shippingDate = shippingDate;
        this.total = total;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public Long getOrderStatusId() {
        return orderStatusId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public LocalDateTime getShippingDate() {
        return shippingDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public static class Builder {
        private Long userId;
        private Long orderDetailId;
        private Long orderStatusId;
        private LocalDateTime orderDate;
        private LocalDateTime shippingDate;
        private BigDecimal total;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder orderDetailId(Long orderDetailId) {
            this.orderDetailId = orderDetailId;
            return this;
        }

        public Builder orderStatusId(Long orderStatusId) {
            this.orderStatusId = orderStatusId;
            return this;
        }

        public Builder orderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder shippingDate(LocalDateTime shippingDate) {
            this.shippingDate = shippingDate;
            return this;
        }

        public Builder total(BigDecimal total) {
            this.total = total;
            return this;
        }

        public Order build() {
            return new Order(userId, orderDetailId, orderStatusId, orderDate, shippingDate, total);
        }
    }
}
