package kz.cake.web.repository;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.Order;
import kz.cake.web.repository.base.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository extends BaseRepository<Order> {
    public OrderRepository() {
        supplier = () -> new Order();
    }

    public Optional<Order> get(Long statusId, Long userId) {
        Order entity = null;
        String sql = String.format("select * from web.orders where order_status_id=%d and user_id=%d", statusId, userId);

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entity = new Order.Builder()
                        .id(resultSet.getLong("id"))
                        .active(resultSet.getBoolean("active"))
                        .userId(resultSet.getLong("user_id"))
                        .orderStatusId(resultSet.getLong("order_status_id"))
                        .orderDate(resultSet.getTimestamp("order_date"))
                        .shippingDate(resultSet.getTimestamp("shipping_date"))
                        .build();
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return Optional.ofNullable(entity);
    }

    @Override
    public List<Order> getAll() {
        List<Order> list = new ArrayList<>();
        Order entity = this.supplier.get();
        String sql = String.format("%s where active=true and order_status_id in (select id from order_status where code <> 'draft')", entity.getReadSql());

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
