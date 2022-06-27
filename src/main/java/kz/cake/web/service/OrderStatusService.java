package kz.cake.web.service;

import kz.cake.web.entity.Local;
import kz.cake.web.entity.OrderStatus;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.repository.OrderStatusRepository;
import kz.cake.web.service.base.DictionaryService;

import java.util.Optional;

public class OrderStatusService extends DictionaryService<OrderStatus, OrderStatusRepository> {
    public OrderStatusService() {
        super();
        this.repository = new OrderStatusRepository();
        this.supplier = () -> new OrderStatus();
    }

    @Override
    public String cacheKey() {
        return "OrderStatus";
    }

    @Override
    public String cacheKeyWithLocal() {
        return "OrderStatusWithLocal";
    }

    @Override
    protected DictionaryDto mapWithLocal(OrderStatus m) {
        DictionaryDto dictionaryDto = new DictionaryDto(m.getId(), m.getCode(), m.isActive());
        Optional<Local> local = localService.findByCode(m.getCode());
        if (local.isPresent()) {
            dictionaryDto.setText(local.get().getMessage());
        }
        return dictionaryDto;
    }

    @Override
    protected DictionaryDto map(OrderStatus m) {
        return new DictionaryDto(m.getId(), m.getCode(), m.isActive());
    }
}
