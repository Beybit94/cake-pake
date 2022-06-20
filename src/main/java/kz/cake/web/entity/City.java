package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class City extends BaseDictionary<Long> {
    public City() {
        super();
    }

    private City(String localId) {
        super(localId);
    }

    @Override
    public String getTableName() {
        return "web.city";
    }

    public static class Builder {
        private String localId;

        public Builder() {
        }

        public Builder localId(String localId) {
            this.localId = localId;
            return this;
        }

        public City build() {
            return new City(localId);
        }
    }
}
