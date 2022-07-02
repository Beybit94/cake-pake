package kz.cake.web.repository;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.ProductComment;
import kz.cake.web.repository.base.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductCommentRepository extends BaseRepository<ProductComment> {
    public ProductCommentRepository() {
        supplier = () -> new ProductComment();
    }

    public List<ProductComment> getByProductId(Long id) {
        List<ProductComment> list = new ArrayList<>();
        ProductComment entity = this.supplier.get();
        String sql = String.format("%s where active=true and product_id=%d", entity.getReadSql(), id);

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
}
