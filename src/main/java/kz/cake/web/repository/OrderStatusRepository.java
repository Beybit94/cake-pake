package kz.cake.web.repository;

import kz.cake.web.entity.OrderStatus;
import kz.cake.web.repository.base.BaseRepository;

public class OrderStatusRepository extends BaseRepository<OrderStatus> {
    public OrderStatusRepository() {
        supplier = () -> new OrderStatus();
    }
}
