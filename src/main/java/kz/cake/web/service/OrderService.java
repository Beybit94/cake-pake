package kz.cake.web.service;

import kz.cake.web.entity.Order;
import kz.cake.web.entity.OrderDetail;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.helpers.constants.LocaleCodes;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.OrderDetailDto;
import kz.cake.web.model.OrderDto;
import kz.cake.web.repository.OrderRepository;
import kz.cake.web.service.base.BaseService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderService extends BaseService<Order, OrderRepository> {
    private final UserService userService;
    private final OrderDetailService orderDetailService;
    private final OrderStatusService orderStatusService;

    public OrderService() {
        this.repository = new OrderRepository();
        userService = new UserService();
        orderDetailService = new OrderDetailService();
        orderStatusService = new OrderStatusService();
    }

    public void save(OrderDto orderDto) throws SQLException, IllegalAccessException, CustomValidationException {
        Order order = super.save(new Order.Builder()
                .id(orderDto.getId())
                .userId(CurrentSession.Instance.getCurrentUser().getUserId())
                .orderStatusId(orderDto.getOrderStatus().getId())
                .address(orderDto.getAddress())
                .paymentType(orderDto.getPaymentType())
                .orderDate(orderDto.getOrderDate())
                .shippingDate(orderDto.getShippingDate())
                .active(orderDto.isActive())
                .build());

        for (OrderDetailDto orderDetailDto : orderDto.getOrderDetail()) {
            OrderDetail orderDetail = new OrderDetail.Builder()
                    .id(orderDetailDto.getId())
                    .orderId(order.getId())
                    .productId(orderDetailDto.getProduct().getId())
                    .quantity(orderDetailDto.getQuantity())
                    .active(true)
                    .build();

            if (orderDetail.getQuantity() <= 0) {
                orderDetailService.delete(orderDetail);
            } else {
                orderDetailService.save(new OrderDetail.Builder()
                        .id(orderDetailDto.getId())
                        .orderId(order.getId())
                        .productId(orderDetailDto.getProduct().getId())
                        .quantity(orderDetailDto.getQuantity())
                        .active(true)
                        .build());
            }
        }
    }

    public Optional<OrderDto> getDraft() {
        Optional<DictionaryDto> draftStatus = orderStatusService.findByCode(LocaleCodes.statusDraft.getName());
        return getByStatus(draftStatus.get().getId());
    }

    public Optional<OrderDto> getByStatus(Long id) {
        OrderDto orderDto = null;
        Optional<Order> order = repository.get(id, CurrentSession.Instance.getCurrentUser().getUserId());
        if (order.isPresent()) {
            orderDto = map(order.get());
        }

        return Optional.ofNullable(orderDto);
    }

    public List<OrderDto> getOrders() {
        return repository.getAll().stream().map(m -> map(m)).collect(Collectors.toList());
    }

    public List<OrderDto> getOrderHistory() {
        return repository.getHistory().stream().map(m -> map(m)).collect(Collectors.toList());
    }

    private OrderDto map(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setActive(order.isActive());
        orderDto.setAddress(order.getAddress());
        orderDto.setPaymentType(order.getPaymentType());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setShippingDate(order.getShippingDate());

        if (order.getOrderDate() != null) {
            orderDto.setOrderDateText(StringUtils.localDateString(order.getOrderDate()));
        }

        if (order.getShippingDate() != null) {
            orderDto.setShippingDateText(StringUtils.localDateString(order.getShippingDate()));
        }

        if (order.getUserId() != null) {
            orderDto.setUser(userService.read(order.getUserId()));
        }

        if (order.getOrderStatusId() != null) {
            orderDto.setOrderStatus(orderStatusService.getByIdWithLocal(order.getOrderStatusId()));
        }

        orderDto.setOrderDetail(orderDetailService.getByOrderId(order.getId()));
        return orderDto;
    }
}
