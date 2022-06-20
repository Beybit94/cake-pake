package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class ProductSize extends BaseDictionary<Long> {
    public ProductSize() {
        super();
    }

    private ProductSize(String code) {
        super(code);
    }

    @Override
    public String getTableName() {
        return "web.product_size";
    }

    public static class Builder {
        private String code;

        public Builder() {
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public ProductSize build() {
            return new ProductSize(code);
        }
    }
}
