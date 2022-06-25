package kz.cake.web.repository;

import kz.cake.web.entity.Order;
import kz.cake.web.repository.base.BaseRepository;

public class OrderRepository extends BaseRepository<Order> {
    public OrderRepository() {
        supplier = () -> new Order();
    }
}
