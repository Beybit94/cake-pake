package kz.cake.web.repository.base;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.base.Base;
import kz.cake.web.helper.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository<T extends Base> implements CrudRepository<T> {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    public void createTable(T table) {
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            String sql = table.getCreateTableSql();
            connection.createStatement().execute(sql);
            logger.info(table.getTableName() + " created");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
    }

    @Override
    public void create(T entity) {
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(entity.getCreateSql())) {
            setQueryParameters(preparedStatement, entity, true);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
    }

    @Override
    public T read(T entity) {
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(entity.getReadSql())) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    setFields(entity, resultSet);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
        return null;
    }

    @Override
    public void update(T entity) {
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(entity.getUpdateSql())) {
            setQueryParameters(preparedStatement, entity, true);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
    }

    @Override
    public void delete(T entity) {
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(entity.getDeleteSql())) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
    }

    private Map<Integer, Object> getFields(T t, boolean skipFirst) throws IllegalAccessException {
        Map<Integer, Object> result = new HashMap<>();
        Field[] fields = t.getClass().getDeclaredFields();

        int iterator = 0;
        for (int i = 0; i < fields.length; i++) {
            if (skipFirst && i == 0) continue;

            Field field = fields[i];
            field.setAccessible(true);
            result.put(iterator, field.get(t));
            iterator++;
        }
        return result;
    }

    private void setFields(T t, ResultSet resultSet) throws SQLException, NoSuchFieldException, IllegalAccessException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int column = 0; column < columnCount; column++) {
            String columnName = resultSetMetaData.getColumnName(column);
            Object value = resultSet.getObject(column);
            setField(t, StringUtils.getFieldName(columnName), value);
        }
    }

    private void setField(T t, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class<?> tClass = t.getClass();
        Field field = tClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(t, value);
    }

    private void setQueryParameters(PreparedStatement preparedStatement, T t, boolean skipFirst) throws IllegalAccessException, SQLException {
        Map<Integer, Object> fields = getFields(t, skipFirst);
        for (Integer i : fields.keySet()) {
            Object value = fields.get(i);
            if (value instanceof Long) {
                preparedStatement.setLong(i, (Long) value);
            } else if (value instanceof Integer) {
                preparedStatement.setInt(i, (Integer) value);
            } else if (value instanceof Double) {
                preparedStatement.setDouble(i, (Double) value);
            } else if (value instanceof BigDecimal) {
                preparedStatement.setBigDecimal(i, (BigDecimal) value);
            } else if (value instanceof LocalDateTime) {
                preparedStatement.setTimestamp(i, Timestamp.valueOf((LocalDateTime) value));
            } else if (value instanceof Boolean) {
                preparedStatement.setBoolean(i, (Boolean) value);
            } else {
                preparedStatement.setString(i, value.toString());
            }
        }
    }
}
