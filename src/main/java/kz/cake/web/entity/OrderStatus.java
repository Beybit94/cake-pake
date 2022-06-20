package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class OrderStatus extends BaseDictionary<Long> {
    public OrderStatus() {
        super();
    }

    private OrderStatus(String code) {
        super(code);
    }

    @Override
    public String getTableName() {
        return "web.order_status";
    }

    public static class Builder {
        private String code;

        public Builder() {
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public OrderStatus build() {
            return new OrderStatus(code);
        }
    }
}
