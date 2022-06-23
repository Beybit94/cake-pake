package kz.cake.web.entity;


import kz.cake.web.entity.base.Base;

public class Local extends Base<Long> {
    private String code;
    private String message;
    private Long languageId;

    public Local() {
        super();
    }

    private Local(Long id, boolean active, String code, String message, Long languageId) {
        this.code = code;
        this.message = message;
        this.languageId = languageId;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Long getLanguageId() {
        return languageId;
    }

    @Override
    public String getTableName() {
        return "web.local";
    }

    @Override
    public String getParameters() {
        return "id,active,code,message,language_id";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "code varchar(100) NOT NULL," +
                "message text NOT NULL," +
                "language_id bigint NOT NULL," +
                "active boolean DEFAULT true not null," +
                "FOREIGN KEY (language_id) REFERENCES web.languages (id)," +
                "UNIQUE (code, language_id)" +
                ");", getTableName());
    }

    public static class Builder {
        private Long id;
        private boolean active;
        private String code;
        private String message;
        private Long languageId;

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

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder languageId(Long languageId) {
            this.languageId = languageId;
            return this;
        }

        public Local build() {
            return new Local(id, active, code, message, languageId);
        }
    }
}
