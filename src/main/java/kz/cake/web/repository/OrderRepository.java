package kz.cake.web.repository;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.Order;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CurrentSession;
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
                        .address(resultSet.getString("address"))
                        .paymentType(resultSet.getString("payment_type"))
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
        String sql = String.format("select o.* from web.orders o " +
                "left join web.users u on u.id = o.user_id " +
                "where o.active=true " +
                "and u.active=true " +
                "and o.order_status_id in (select id from web.order_status where code <> 'draft') " +
                "and o.id in (select order_id from web.order_details where product_id in (select id from web.products where user_id=%d and active=true))", CurrentSession.Instance.getCurrentUser().getUserId());

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Order.Builder()
                        .id(resultSet.getLong("id"))
                        .active(resultSet.getBoolean("active"))
                        .userId(resultSet.getLong("user_id"))
                        .address(resultSet.getString("address"))
                        .paymentType(resultSet.getString("payment_type"))
                        .orderStatusId(resultSet.getLong("order_status_id"))
                        .orderDate(resultSet.getTimestamp("order_date"))
                        .shippingDate(resultSet.getTimestamp("shipping_date"))
                        .build());
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return list;
    }

    public List<Order> getHistory() {
        List<Order> list = new ArrayList<>();
        String sql = String.format("select * from web.orders " +
                "where active=true " +
                "and order_status_id in (select id from web.order_status where code <> 'draft') " +
                "and user_id=%d", CurrentSession.Instance.getCurrentUser().getUserId());

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Order.Builder()
                        .id(resultSet.getLong("id"))
                        .active(resultSet.getBoolean("active"))
                        .userId(resultSet.getLong("user_id"))
                        .address(resultSet.getString("address"))
                        .paymentType(resultSet.getString("payment_type"))
                        .orderStatusId(resultSet.getLong("order_status_id"))
                        .orderDate(resultSet.getTimestamp("order_date"))
                        .shippingDate(resultSet.getTimestamp("shipping_date"))
                        .build());
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return list;
    }
}
