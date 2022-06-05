package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class City extends BaseDictionary<Long> {
    public City() {
        super();
    }

    private City(Long localId) {
        super(localId);
    }

    @Override
    public String getTableName() {
        return "web.city";
    }

    public static class Builder {
        private Long localId;

        public Builder() {
        }

        public Builder localId(Long localId) {
            this.localId = localId;
            return this;
        }

        public City build() {
            return new City(localId);
        }
    }
}
