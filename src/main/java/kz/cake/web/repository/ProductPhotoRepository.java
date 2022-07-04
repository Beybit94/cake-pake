package kz.cake.web.repository;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.ProductPhoto;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.repository.base.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductPhotoRepository extends BaseRepository<ProductPhoto> {
    public ProductPhotoRepository() {
        supplier = () -> new ProductPhoto();
    }

    public List<ProductPhoto> getAllByProduct(Long productId) {
        List<ProductPhoto> list = new ArrayList<>();
        ProductPhoto entity = this.supplier.get();
        String sql = String.format("%s where active=true and product_id = %d", entity.getReadSql(), productId);

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

    @Override
    public void delete(ProductPhoto entity) throws CustomValidationException {
        Connection connection = BasicConnectionPool.Instance.getConnection();
        String sql = String.format("delete from %s where id=%d", entity.getTableName(), entity.getId());

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
    }
}
