package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class Role extends BaseDictionary<Long> {
    public Role() {
        super();
    }

    private Role(Long id, boolean active, String code) {
        super(code);
        this.id = id;
        this.active = active;
    }

    @Override
    public String getTableName() {
        return "web.roles";
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

        public Role build() {
            return new Role(id, active, code);
        }
    }
}
