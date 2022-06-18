package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class ProductCategory extends BaseDictionary<Long> {
    private Long parent;

    public ProductCategory() {
        super();
    }

    protected ProductCategory(Long localId, Long parent) {
        super(localId);
        this.parent = parent;
    }

    public Long getParent() {
        return parent;
    }

    @Override
    public String getTableName() {
        return "web.product_category";
    }

    @Override
    public String getParameters() {
        return "id,active,parent,localId";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "parent bigint NULL," +
                "local_id bigint NOT NULL," +
                "active boolean DEFAULT true not null," +
                "FOREIGN KEY (local_id) REFERENCES web.local (id)" +
                ");", getTableName());
    }

    public static class Builder {
        private Long parent;
        private Long localId;

        public Builder() {
        }

        public Builder localId(Long localId) {
            this.localId = localId;
            return this;
        }

        public Builder parent(Long parent) {
            this.parent = parent;
            return this;
        }

        public ProductCategory build() {
            return new ProductCategory(localId, parent);
        }
    }
}
