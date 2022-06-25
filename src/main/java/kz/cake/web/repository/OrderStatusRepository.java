package kz.cake.web.repository;

import kz.cake.web.entity.OrderStatus;
import kz.cake.web.repository.base.DictionaryRepository;

public class OrderStatusRepository extends DictionaryRepository<OrderStatus> {
    public OrderStatusRepository() {
        supplier = () -> new OrderStatus();
    }
}
