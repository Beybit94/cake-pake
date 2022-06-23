package kz.cake.web.repository.base;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.base.Base;
import kz.cake.web.helpers.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public abstract class BaseRepository<T extends Base> implements CrudRepository<T> {
    private final Logger logger = LogManager.getLogger(BaseRepository.class);

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
            e.printStackTrace();
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

    @Override
    public List<T> getAll() {
        return null;
    }

    protected Map<Integer, Object> getFields(T t, boolean skipFirst) throws IllegalAccessException {
        Map<Integer, Object> result = new HashMap<>();
        Field[] fields = getAllFields(t.getClass());

        int iterator = 1;
        for (int i = 0; i < fields.length; i++) {
            if (skipFirst && i == 0) continue;

            Field field = fields[i];
            field.setAccessible(true);
            result.put(iterator, field.get(t));
            iterator++;
        }

        return result;
    }

    protected Field[] getAllFields(Class klass) {
        List<Field> fields = new ArrayList<>();
        if (klass.getSuperclass() != null) {
            fields.addAll(Arrays.asList(getAllFields(klass.getSuperclass())));
        }
        fields.addAll(Arrays.asList(klass.getDeclaredFields()));
        return fields.toArray(new Field[]{});
    }

    protected void setFields(T t, ResultSet resultSet) throws SQLException, NoSuchFieldException, IllegalAccessException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int column = 0; column < columnCount; column++) {
            String columnName = resultSetMetaData.getColumnName(column);
            Object value = resultSet.getObject(column);
            setField(t, StringUtils.getFieldName(columnName), value);
        }
    }

    protected void setField(T t, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class<?> tClass = t.getClass();
        Field field = tClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(t, value);
    }

    protected void setQueryParameters(PreparedStatement preparedStatement, T t, boolean skipFirst) throws IllegalAccessException, SQLException {
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
            } else if (value instanceof Boolean) {
                preparedStatement.setBoolean(i, (Boolean) value);
            } else if (value instanceof String) {
                preparedStatement.setString(i, (String) value);
            } else {
                preparedStatement.setObject(i, value);
            }
        }
    }
}
