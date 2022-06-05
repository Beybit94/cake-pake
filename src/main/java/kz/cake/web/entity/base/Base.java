package kz.cake.web.entity.base;

public abstract class Base<T> {
    private T id;
    private boolean active;

    protected Base() {
        active = true;
    }

    public T getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract String getTableName();

    public abstract String getParameters();

    public abstract String getCreateTableSql();
}
