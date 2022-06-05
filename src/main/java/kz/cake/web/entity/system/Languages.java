package kz.cake.web.entity.system;

public class Languages extends System<Long> {
    private String code;

    public Languages() {
        super();
    }

    private Languages(String code){
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
        return "id,code,active";
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
        private String code;

        public Builder() {
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Languages build() {
            return new Languages(code);
        }
    }
}
