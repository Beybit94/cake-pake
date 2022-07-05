package kz.cake.web.repository;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.OrderDetail;
import kz.cake.web.entity.Product;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.repository.base.BaseRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository extends BaseRepository<Product> {
    private final OrderDetailRepository orderDetailRepository;

    public ProductRepository() {
        supplier = () -> new Product();
        orderDetailRepository = new OrderDetailRepository();
    }

    @Override
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        String sql = String.format("select p.* from web.products p " +
                "left join web.users u on u.id = p.user_id " +
                "where p.active=true " +
                "and u.active=true");

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Product.Builder()
                        .id(resultSet.getLong("id"))
                        .active(resultSet.getBoolean("active"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .price(resultSet.getBigDecimal("price"))
                        .userId(resultSet.getLong("user_id"))
                        .cityId(resultSet.getLong("city_id"))
                        .sizeId(resultSet.getLong("size_id"))
                        .categoryId(resultSet.getLong("category_id"))
                        .build());
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return list;
    }

    @Override
    public void delete(Product entity) throws CustomValidationException {
        Optional<OrderDetail> find = orderDetailRepository.getAll().stream().filter(m -> m.getProductId().equals(entity.getId())).findAny();
        if (find.isPresent()) {
            throw new CustomValidationException("error.activeRecord", ActionNames.ProductMy);
        }
        super.delete(entity);
    }

}
