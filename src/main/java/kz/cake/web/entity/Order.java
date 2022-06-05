package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order extends Base<Long> {
    private Long userId;
    private Long orderDetailId;
    private Long orderStatusId;
    private LocalDateTime orderDate;
    private LocalDateTime shippingDate;
    private BigDecimal total;

    public Order() {
        super();
    }

    private Order(Long userId, Long orderDetailId, Long orderStatusId, LocalDateTime orderDate, LocalDateTime shippingDate, BigDecimal total) {
        this.userId = userId;
        this.orderDetailId = orderDetailId;
        this.orderStatusId = orderStatusId;
        this.orderDate = orderDate;
        this.shippingDate = shippingDate;
        this.total = total;
    }

    @Override
    public String getTableName() {
        return "web.orders";
    }

    @Override
    public String getParameters() {
        return "id,user_id,order_detail_id,order_status_id,order_date,shipping_date,total,active";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "user_id bigint NOT NULL," +
                "order_detail_id bigint NOT NULL," +
                "order_status_id bigint NOT NULL," +
                "order_date timestamp NOT NULL DEFAULT now()," +
                "shipping_date timestamp," +
                "total numeric," +
                "active boolean not null DEFAULT true," +
                "FOREIGN KEY (user_id) REFERENCES web.users (id)," +
                "FOREIGN KEY (order_detail_id) REFERENCES web.order_details (id)," +
                "FOREIGN KEY (order_status_id) REFERENCES web.order_status (id)" +
                ");", getTableName());
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
