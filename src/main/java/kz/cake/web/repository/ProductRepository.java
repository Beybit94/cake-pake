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
import java.util.Optional;

public class ProductRepository extends BaseRepository<Product> {
    private final OrderDetailRepository orderDetailRepository;

    public ProductRepository() {
        supplier = () -> new Product();
        orderDetailRepository = new OrderDetailRepository();
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
