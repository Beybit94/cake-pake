package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class ProductSize extends BaseDictionary<Long> {
    public ProductSize() {
        super();
    }

    private ProductSize(Long localId) {
        super(localId);
    }

    @Override
    public String getTableName() {
        return "web.product_size";
    }

    public static class Builder {
        private Long localId;

        public Builder() {
        }

        public Builder localId(Long localId) {
            this.localId = localId;
            return this;
        }

        public ProductSize build() {
            return new ProductSize(localId);
        }
    }
}
