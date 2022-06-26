package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class OrderStatus extends BaseDictionary<Long> {
    public OrderStatus() {
        super();
    }

    private OrderStatus(Long id, boolean active, String code) {
        super(code);
        this.id = id;
        this.active = active;
    }

    @Override
    public String getTableName() {
        return "web.order_status";
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

        public OrderStatus build() {
            return new OrderStatus(id, active, code);
        }
    }
}
