package kz.cake.web.entity.base;

public abstract class BaseDictionary<T> extends Base<T> {
    private Long localId;

    public BaseDictionary() {
        super();
    }

    protected BaseDictionary(Long localId) {
        this.localId = localId;
    }

    public Long getLocalId() {
        return localId;
    }

    @Override
    public String getParameters() {
        return "id,active,local_id";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "local_id bigint NOT NULL," +
                "active boolean DEFAULT true not null," +
                "FOREIGN KEY (local_id) REFERENCES web.local (id)" +
                ");", getTableName());
    }
}
