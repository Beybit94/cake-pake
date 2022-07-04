package kz.cake.web.service;

import kz.cake.web.entity.OrderDetail;
import kz.cake.web.model.OrderDetailDto;
import kz.cake.web.repository.OrderDetailRepository;
import kz.cake.web.service.base.BaseService;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailService extends BaseService<OrderDetail, OrderDetailRepository> {
    private final ProductService productService;

    public OrderDetailService() {
        this.repository = new OrderDetailRepository();
        productService = new ProductService();
    }

    public List<OrderDetailDto> getByOrderId(Long id) {
        return repository.getByOrderId(id).stream().map(m -> map(m)).collect(Collectors.toList());
    }

    private OrderDetailDto map(OrderDetail orderDetail) {
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setId(orderDetail.getId());
        orderDetailDto.setActive(orderDetail.isActive());
        orderDetailDto.setOrderId(orderDetail.getOrderId());
        orderDetailDto.setQuantity(orderDetail.getQuantity());

        if (orderDetail.getProductId() != null) {
            orderDetailDto.setProduct(productService.getById(orderDetail.getProductId()));
        }

        return orderDetailDto;
    }
}
