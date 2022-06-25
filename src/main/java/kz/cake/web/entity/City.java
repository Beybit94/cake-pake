package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class City extends BaseDictionary<Long> {
    public City() {
        super();
    }

    private City(Long id, boolean active, String code) {
        super(code);
        this.id = id;
        this.active = active;
    }

    @Override
    public String getTableName() {
        return "web.city";
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

        public City build() {
            return new City(id, active, code);
        }
    }
}
