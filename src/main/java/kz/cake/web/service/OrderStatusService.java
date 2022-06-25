package kz.cake.web.service;

import kz.cake.web.entity.OrderStatus;
import kz.cake.web.repository.OrderStatusRepository;
import kz.cake.web.service.base.DictionaryService;

public class OrderStatusService extends DictionaryService<OrderStatus, OrderStatusRepository> {
    @Override
    public String cacheKey() {
        return "OrderStatus";
    }

    @Override
    public String cacheKeyWithLocal() {
        return "OrderStatusWithLocal";
    }
}
