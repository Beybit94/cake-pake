package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

public class OrderDetail extends Base<Long> {
    private Long orderId;
    private Long productId;
    private Integer quantity;

    public OrderDetail() {
        super();
    }

    private OrderDetail(Long id, boolean active, Long orderId, Long productId, Integer quantity) {
        this.id = id;
        this.active = active;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public String getTableName() {
        return "web.order_details";
    }

    @Override
    public String getParameters() {
        return "id,active,order_id,product_id,quantity";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "order_id bigint NOT NULL," +
                "product_id bigint NOT NULL," +
                "quantity int," +
                "active boolean not null DEFAULT true," +
                "FOREIGN KEY (order_id) REFERENCES web.orders (id)," +
                "FOREIGN KEY (product_id) REFERENCES web.products (id)" +
                ");" +
                "CREATE UNIQUE INDEX unique_product_order_detail ON %s(order_id, product_id) WHERE active;", getTableName(), getTableName());
    }

    public Long getOrderId() {
        return orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", id=" + id +
                ", active=" + active +
                '}';
    }

    public static class Builder {
        private Long id;

        private boolean active;

        private Long orderId;
        private Long productId;
        private Integer quantity;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(id, active, orderId, productId, quantity);
        }
    }
}
