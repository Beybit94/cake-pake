package kz.cake.web.entity.system;

public class Local extends System<Long> {
    private String message;
    private Long languageId;

    public Local() {
        super();
    }

    private Local(String message, Long languageId) {
        this.message = message;
        this.languageId = languageId;
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
        return "id,active,message,language_id";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "message text NOT NULL," +
                "language_id bigint NOT NULL," +
                "active boolean DEFAULT true not null," +
                "FOREIGN KEY (language_id) REFERENCES web.languages (id)" +
                ");", getTableName());
    }

    public static class Builder {
        private String message;
        private Long languageId;

        public Builder() {
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
            return new Local(message, languageId);
        }
    }
}
