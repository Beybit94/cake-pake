package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class Role extends BaseDictionary<Long> {
    public Role() {
        super();
    }

    private Role(Long localId) {
        super(localId);
    }

    @Override
    public String getTableName() {
        return "web.roles";
    }

    public static class Builder {
        private Long localId;

        public Builder() {
        }

        public Builder localId(Long localId) {
            this.localId = localId;
            return this;
        }

        public Role build() {
            return new Role(localId);
        }
    }
}
