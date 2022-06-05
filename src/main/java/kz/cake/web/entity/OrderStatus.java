package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class OrderStatus extends BaseDictionary<Long> {
    public OrderStatus() {
        super();
    }

    private OrderStatus(Long localId) {
        super(localId);
    }

    @Override
    public String getTableName() {
        return "web.order_status";
    }

    public static class Builder {
        private Long localId;

        public Builder() {
        }

        public Builder localId(Long localId) {
            this.localId = localId;
            return this;
        }

        public OrderStatus build() {
            return new OrderStatus(localId);
        }
    }
}
