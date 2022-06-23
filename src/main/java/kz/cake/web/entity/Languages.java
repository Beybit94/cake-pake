package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

public class Languages extends Base<Long> {
    private String code;

    public Languages() {
        super();
    }

    private Languages(Long id, boolean active, String code) {
        this.id = id;
        this.active = active;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getTableName() {
        return "web.languages";
    }

    @Override
    public String getParameters() {
        return "id,active,code";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "code varchar(20) UNIQUE NOT NULL," +
                "active boolean DEFAULT true not null" +
                ");", getTableName());
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

        public Languages build() {
            return new Languages(id, active, code);
        }
    }
}
