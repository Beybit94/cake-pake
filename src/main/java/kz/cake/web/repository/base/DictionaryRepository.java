package kz.cake.web.repository.base;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.base.BaseDictionary;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public abstract class DictionaryRepository<T extends BaseDictionary> extends BaseRepository<T> {
    private final Logger logger = LogManager.getLogger(DictionaryRepository.class);

    public DictionaryRepository() {

    }

    public Optional<T> findByCode(String code) {
        T entity = supplier.get();
        String sql = String.format("%s where code=?", entity.getReadSql());

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                setFields(entity, resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return Optional.ofNullable(entity);
    }

    public T getById(Long id) {
        T entity = supplier.get();
        String sql = String.format("%s where id=?", entity.getReadSql());

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
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
}
