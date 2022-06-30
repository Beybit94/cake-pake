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
    private Long cityId;

    public Product() {
        super();
    }

    private Product(Long id, String name, String description, BigDecimal price, Long userId, Long categoryId, Long sizeId, Long cityId, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.userId = userId;
        this.categoryId = categoryId;
        this.sizeId = sizeId;
        this.cityId = cityId;
        this.active = active;
    }

    @Override
    public String getTableName() {
        return "web.products";
    }

    @Override
    public String getParameters() {
        return "id,active,name,description,price,user_id,category_id,size_id,city_id";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "name varchar(150) NOT NULL," +
                "price numeric NOT NULL," +
                "description text," +
                "user_id bigint NOT NULL," +
                "category_id bigint NOT NULL," +
                "size_id bigint NOT NULL," +
                "city_id bigint NOT NULL," +
                "active boolean not null DEFAULT true," +
                "FOREIGN KEY (user_id) REFERENCES web.users (id)," +
                "FOREIGN KEY (category_id) REFERENCES web.product_category (id)," +
                "FOREIGN KEY (size_id) REFERENCES web.product_size (id)," +
                "FOREIGN KEY (city_id) REFERENCES web.city (id)" +
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

    public Long getCityId() {
        return cityId;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Long userId;
        private Long categoryId;
        private Long sizeId;
        private Long cityId;
        private boolean active;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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

        public Builder cityId(Long cityId) {
            this.cityId = cityId;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Product build() {
            return new Product(id, name, description, price, userId, categoryId, sizeId, cityId, active);
        }
    }
}
