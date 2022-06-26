package kz.cake.web.entity;

import kz.cake.web.entity.base.BaseDictionary;

public class ProductCategory extends BaseDictionary<Long> {
    private Long parent;

    public ProductCategory() {
        super();
    }

    protected ProductCategory(Long id, boolean active, String code, Long parent) {
        super(code);
        this.id = id;
        this.active = active;
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
        return "id,active,code,parent";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "parent bigint NULL," +
                "code varchar(100) NOT NULL," +
                "active boolean DEFAULT true not null" +
                ");", getTableName());
    }

    public static class Builder {
        private Long id;
        private boolean active;
        private Long parent;
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

        public Builder parent(Long parent) {
            this.parent = parent;
            return this;
        }

        public ProductCategory build() {
            return new ProductCategory(id, active, code, parent);
        }
    }
}
