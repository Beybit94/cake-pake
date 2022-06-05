package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

import java.math.BigDecimal;

public class Product extends Base<Long> {
    private String name;
    private String description;
    private BigDecimal price;
    private Long userId;
    private Long categoryId;
    private Long sizeId;

    public Product() {
        super();
    }

    private Product(String name, String description, BigDecimal price, Long userId, Long categoryId, Long sizeId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.userId = userId;
        this.categoryId = categoryId;
        this.sizeId = sizeId;
    }

    @Override
    public String getTableName() {
        return "web.products";
    }

    @Override
    public String getParameters() {
        return "id,user_id,category_id,size_id,price,description,active";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "user_id bigint NOT NULL," +
                "category_id bigint NOT NULL," +
                "size_id bigint NOT NULL," +
                "price numeric," +
                "description text," +
                "active boolean not null DEFAULT true," +
                "FOREIGN KEY (user_id) REFERENCES web.users (id)," +
                "FOREIGN KEY (category_id) REFERENCES web.product_category (id)," +
                "FOREIGN KEY (size_id) REFERENCES web.product_size (id)" +
                ");", getTableName());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public static class Builder {
        private String name;
        private String description;
        private BigDecimal price;
        private Long userId;
        private Long categoryId;
        private Long sizeId;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder categoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder sizeId(Long sizeId) {
            this.sizeId = sizeId;
            return this;
        }

        public Product build() {
            return new Product(name, description, price, userId, categoryId, sizeId);
        }
    }
}
