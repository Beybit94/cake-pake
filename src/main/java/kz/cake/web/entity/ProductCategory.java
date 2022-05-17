package kz.cake.web.entity;

public class ProductCategory extends BaseDictionary<Long> {
    private Long parent;

    protected ProductCategory(String code, Long parent) {
        super(code);
    }

    public Long getParent() {
        return parent;
    }

    public static class Builder {
        private String code;
        private Long parent;

        public Builder() {
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder parent(Long parent) {
            this.parent = parent;
            return this;
        }

        public ProductCategory build() {
            return new ProductCategory(code, parent);
        }
    }
}
