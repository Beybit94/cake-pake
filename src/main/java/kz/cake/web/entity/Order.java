package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

import java.sql.Timestamp;

public class Order extends Base<Long> {
    private Long userId;
    private Long orderStatusId;
    private Timestamp orderDate;
    private Timestamp shippingDate;

    public Order() {
        super();
    }

    private Order(Long id, boolean active, Long userId, Long orderStatusId, Timestamp orderDate, Timestamp shippingDate) {
        this.id = id;
        this.active = active;
        this.userId = userId;
        this.orderStatusId = orderStatusId;
        this.orderDate = orderDate;
        this.shippingDate = shippingDate;
    }

    @Override
    public String getTableName() {
        return "web.orders";
    }

    @Override
    public String getParameters() {
        return "id,active,user_id,order_status_id,order_date,shipping_date";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" + "id bigserial PRIMARY KEY," + "user_id bigint NOT NULL," + "order_status_id bigint NOT NULL," + "order_date timestamp NOT NULL DEFAULT now()," + "shipping_date timestamp," + "active boolean not null DEFAULT true," + "FOREIGN KEY (user_id) REFERENCES web.users (id)," + "FOREIGN KEY (order_status_id) REFERENCES web.order_status (id)" + ");", getTableName());
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOrderStatusId() {
        return orderStatusId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public Timestamp getShippingDate() {
        return shippingDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId=" + userId +
                ", orderStatusId=" + orderStatusId +
                ", orderDate=" + orderDate +
                ", shippingDate=" + shippingDate +
                ", id=" + id +
                ", active=" + active +
                '}';
    }

    public static class Builder {
        private Long id;

        private boolean active;
        private Long userId;
        private Long orderStatusId;
        private Timestamp orderDate;
        private Timestamp shippingDate;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder orderStatusId(Long orderStatusId) {
            this.orderStatusId = orderStatusId;
            return this;
        }

        public Builder orderDate(Timestamp orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder shippingDate(Timestamp shippingDate) {
            this.shippingDate = shippingDate;
            return this;
        }

        public Order build() {
            return new Order(id, active, userId, orderStatusId, orderDate, shippingDate);
        }
    }
}
