package kz.cake.web.entity.base;

import kz.cake.web.helpers.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Base<T> {
    protected T id;
    protected boolean active;

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

    public String getCreateSql() {
        String parameters = StringUtils.skipFirstElement(getParameters());
        String valuesStr = String.join(",", Arrays.stream(parameters.split(","))
                .map(p -> "?")
                .collect(Collectors.toList()));
        String sql = String.format("insert into %s(%s) values(%s)", getTableName(), parameters, valuesStr);
        return sql;
    }

    public String getReadSql() {
        return String.format("select %s from %s", getParameters(), getTableName());
    }

    public String getUpdateSql() {
        String parameters = StringUtils.skipFirstElement(getParameters());
        String valuesStr = String.join(",", Arrays.stream(parameters.split(","))
                .map(p -> p + " = ?")
                .collect(Collectors.toList()));
        String sql = String.format("update %s set %s where id=%d", getTableName(), valuesStr, id);
        return sql;
    }

    public String getDeleteSql() {
        return String.format("update %s set active=false where id=%d;", getTableName(), id);
    }
}
