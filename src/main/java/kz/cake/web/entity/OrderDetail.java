package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

public class OrderDetail extends Base<Long> {
    private Long productId;
    private Integer quantity;

    public OrderDetail() {
        super();
    }

    private OrderDetail(Long id, Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public String getTableName() {
        return "web.order_details";
    }

    @Override
    public String getParameters() {
        return "id,product_id,quantity";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "product_id bigint NOT NULL," +
                "quantity int," +
                "active boolean not null DEFAULT true," +
                "FOREIGN KEY (product_id) REFERENCES web.products (id)" +
                ");", getTableName());
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public static class Builder {
        private Long productId;
        private Integer quantity;

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(null, productId, quantity);
        }
    }
}
