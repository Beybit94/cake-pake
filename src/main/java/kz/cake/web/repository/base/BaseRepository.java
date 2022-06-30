package kz.cake.web.repository.base;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.base.Base;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.function.Supplier;

public abstract class BaseRepository<T extends Base> implements CrudRepository<T> {
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected Supplier<T> supplier;

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
    public T create(T entity) throws SQLException, IllegalAccessException {
        String sql = entity.getCreateSql();

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setQueryParameters(preparedStatement, entity, true);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) throw new SQLException();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                    return entity;
                } else {
                    throw new SQLException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
    }

    @Override
    public T read(Long id) {
        T entity = this.supplier.get();
        String sql = String.format("%s where id=%d", entity.getReadSql(), id);

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                setFields(entity, resultSet);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
        return entity;
    }

    @Override
    public T update(T entity) throws SQLException, IllegalAccessException {
        String sql = entity.getUpdateSql();

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setQueryParameters(preparedStatement, entity, true);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) throw new SQLException();

            return entity;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
    }

    @Override
    public void delete(T entity) throws CustomValidationException {
        String sql = entity.getDeleteSql();

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
    }

    @Override
    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        T entity = this.supplier.get();
        String sql = String.format("%s where active=true", entity.getReadSql());

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                setFields(entity, resultSet);
                list.add(entity);
                entity = this.supplier.get();
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return list;
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

    protected Method[] getAllMethods(Class klass) {
        List<Method> methods = new ArrayList<>();
        while (klass != null) {
            methods.addAll(Arrays.asList(klass.getDeclaredMethods()));
            klass = klass.getSuperclass();
        }
        return methods.toArray(new Method[]{});
    }

    protected void setFields(T t, ResultSet resultSet) throws SQLException, IllegalAccessException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            String columnName = resultSetMetaData.getColumnName(column);
            Object value = resultSet.getObject(column);
            setField(t, StringUtils.getFieldName(columnName), value);
        }
    }

    protected void setField(T t, String fieldName, Object value) throws IllegalAccessException {
        Class<?> tClass = t.getClass();
        for (Field field : getAllFields(tClass)) {
            if (field.getName().equals(fieldName)) {
                field.setAccessible(true);
                field.set(t, value);
                break;
            }
        }
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
