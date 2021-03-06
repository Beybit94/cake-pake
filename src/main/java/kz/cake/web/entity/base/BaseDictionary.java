package kz.cake.web.entity.base;

public abstract class BaseDictionary<T> extends Base<T> {
    private String code;

    public BaseDictionary() {
        super();
    }

    protected BaseDictionary(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getParameters() {
        return "id,active,code";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "code varchar(100) NOT NULL," +
                "active boolean DEFAULT true not null" +
                ");" +
                "CREATE UNIQUE INDEX unique_code_%d ON %s(code) WHERE active;", getTableName(), System.currentTimeMillis(), getTableName());
    }
}
