package kz.cake.web.entity;

import java.math.BigDecimal;

public class Product extends Base<Long>{
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private Long sizeId;

    private Product(String name, String description, BigDecimal price, Long categoryId, Long sizeId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.sizeId = sizeId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public static class Builder{
        private String name;
        private String description;
        private BigDecimal price;
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

        public Builder categoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder sizeId(Long sizeId) {
            this.sizeId = sizeId;
            return this;
        }

        public Product build() {
            return new Product(name, description, price, categoryId, sizeId);
        }
    }
}
