package kz.cake.web.entity;

public class OrderStatus extends BaseDictionary<Long> {
    private OrderStatus(String code) {
        super(code);
    }

    public static class Builder {
        private String code;

        public Builder(){}

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public OrderStatus build() {
            return new OrderStatus(code);
        }
    }
}
