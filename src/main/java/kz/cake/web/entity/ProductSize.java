package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class ProductSize extends BaseDictionary<Long> {
    public ProductSize() {
        super();
    }

    private ProductSize(Long id, boolean active, String code) {
        super(code);
        this.id = id;
        this.active = active;
    }

    @Override
    public String getTableName() {
        return "web.product_size";
    }

    public static class Builder {
        private Long id;
        private boolean active;
        private String code;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public ProductSize build() {
            return new ProductSize(id, active, code);
        }
    }
}
