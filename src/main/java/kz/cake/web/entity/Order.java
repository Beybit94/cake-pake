package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

import java.sql.Timestamp;

public class Order extends Base<Long> {
    private Long userId;
    private Long orderStatusId;
    private String address;
    private String paymentType;
    private Timestamp orderDate;
    private Timestamp shippingDate;

    public Order() {
        super();
    }

    private Order(Long id, boolean active, Long userId, Long orderStatusId, String address, String paymentType, Timestamp orderDate, Timestamp shippingDate) {
        this.id = id;
        this.active = active;
        this.userId = userId;
        this.orderStatusId = orderStatusId;
        this.address = address;
        this.paymentType = paymentType;
        this.orderDate = orderDate;
        this.shippingDate = shippingDate;
    }

    @Override
    public String getTableName() {
        return "web.orders";
    }

    @Override
    public String getParameters() {
        return "id,active,user_id,order_status_id,address,payment_type,order_date,shipping_date";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "user_id bigint NOT NULL," +
                "order_status_id bigint NOT NULL," +
                "address varchar(255) NULL," +
                "payment_type varchar(100) NULL," +
                "order_date timestamp," +
                "shipping_date timestamp," +
                "active boolean not null DEFAULT true," +
                "FOREIGN KEY (user_id) REFERENCES web.users (id)," +
                "FOREIGN KEY (order_status_id) REFERENCES web.order_status (id)" +
                ");", getTableName());
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Long orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public void setShippingDate(Timestamp shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getAddress() {
        return address;
    }

    public String getPaymentType() {
        return paymentType;
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
                ", address='" + address + '\'' +
                ", paymentType='" + paymentType + '\'' +
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
        private String address;
        private String paymentType;
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

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder paymentType(String paymentType){
            this.paymentType = paymentType;
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
            return new Order(id, active, userId, orderStatusId, address, paymentType, orderDate, shippingDate);
        }
    }
}
