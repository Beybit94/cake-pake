package kz.cake.web.repository.base;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.base.Base;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseRepository<T extends Base> implements CrudRepository<T> {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    public void createTable(T table) {
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            String sql = table.getCreateTableSql();
            connection.createStatement().execute(sql);
            logger.info(table.getTableName()+" created");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.connect().releaseConnection(connection);
        }
    }

    public String getCreateSql(T table, Map<String, String> values) {
        String valuesStr = String.join(",", Arrays.stream(table.getParameters().split(","))
                .filter(p -> values.containsKey(p))
                .map(p -> "$" + p)
                .collect(Collectors.toList()));
        String sql = String.format("insert into %s(%s) values(%s)", table.getTableName(), table.getParameters(), valuesStr);
        values.keySet().forEach(k -> {
            sql.replace("$" + k, values.get(k));
        });
        return sql;
    }

    public String getReadSql(T table) {
        return String.format("select %s from %s order by id", table.getParameters(), table.getTableName());
    }

    public String getUpdateSql(T table, Map<String, String> values, Long id) {
        String valuesStr = String.join(",", Arrays.stream(table.getParameters().split(","))
                .filter(p -> values.containsKey(p))
                .map(p -> "set " + p + "=$" + p + "\n")
                .collect(Collectors.toList()));
        String sql = String.format("update %s" +
                "\n%s" +
                "where id=%d", table.getTableName(), valuesStr, id);
        values.keySet().forEach(k -> {
            sql.replace("$" + k, values.get(k));
        });
        return sql;
    }

    public String getDeleteSql(T table, Long id) {
        return String.format("update %s set active=false where id=%d;", table.getTableName(), id);
    }
}
