package kz.cake.web.model;

import kz.cake.web.entity.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderDto {
    private Long id;

    private boolean active;

    private Timestamp orderDate;
    private Timestamp shippingDate;
    private String orderDateText;
    private String shippingDateText;
    private User user;
    private DictionaryDto orderStatus;
    private List<OrderDetailDto> orderDetail = new ArrayList<>();

    public OrderDto() {
    }

    public OrderDto(Timestamp orderDate, Timestamp shippingDate, DictionaryDto orderStatus, List<OrderDetailDto> orderDetail) {
        this.orderDate = orderDate;
        this.shippingDate = shippingDate;
        this.orderStatus = orderStatus;
        this.orderDetail = orderDetail;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public Timestamp getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Timestamp shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getOrderDateText() {
        return orderDateText;
    }

    public void setOrderDateText(String orderDateText) {
        this.orderDateText = orderDateText;
    }

    public String getShippingDateText() {
        return shippingDateText;
    }

    public void setShippingDateText(String shippingDateText) {
        this.shippingDateText = shippingDateText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DictionaryDto getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(DictionaryDto orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderDetailDto> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetailDto> orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", shippingDate=" + shippingDate +
                ", orderDateText='" + orderDateText + '\'' +
                ", shippingDateText='" + shippingDateText + '\'' +
                ", user=" + user +
                ", orderStatus=" + orderStatus +
                ", orderDetail=" + orderDetail +
                '}';
    }
}
