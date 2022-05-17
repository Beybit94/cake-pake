package kz.cake.web.entity;

public class ProductSize extends BaseDictionary<Long> {
    private ProductSize(String code) {
        super(code);
    }

    public static class Builder {
        private String code;

        public Builder(){}

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public ProductSize build() {
            return new ProductSize(code);
        }
    }
}
