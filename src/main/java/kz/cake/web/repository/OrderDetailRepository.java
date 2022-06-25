package kz.cake.web.repository;

import kz.cake.web.entity.OrderDetail;
import kz.cake.web.repository.base.BaseRepository;

public class OrderDetailRepository extends BaseRepository<OrderDetail> {
    public OrderDetailRepository() {
        supplier = () -> new OrderDetail();
    }
}
