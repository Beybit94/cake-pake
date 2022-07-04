package kz.cake.web.controller;

import com.google.gson.Gson;
import kz.cake.web.controller.base.BaseController;
import kz.cake.web.entity.Order;
import kz.cake.web.entity.OrderStatus;
import kz.cake.web.entity.Product;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.UrlRouter;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.helpers.constants.LocaleCodes;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.OrderDetailDto;
import kz.cake.web.model.OrderDto;
import kz.cake.web.service.OrderDetailService;
import kz.cake.web.service.OrderService;
import kz.cake.web.service.OrderStatusService;
import kz.cake.web.service.ProductService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OrderController extends BaseController {
    private final OrderService orderService;

    private final ProductService productService;
    private final OrderDetailService orderDetailService;
    private final OrderStatusService orderStatusService;

    public OrderController() {
        orderService = new OrderService();
        productService = new ProductService();
        orderDetailService = new OrderDetailService();
        orderStatusService = new OrderStatusService();
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws SQLException, IllegalAccessException, ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        DictionaryDto status = orderStatusService.findByCode(LocaleCodes.statusNew.getName()).get();

        Order order = orderService.read(id);
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setAddress(request.getParameter("address"));
        order.setPaymentType(request.getParameter("payment"));
        order.setOrderStatusId(status.getId());
        order.setActive(true);
        orderService.save(order);

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParameters.orderDraft.getName(), new OrderDto());

        UrlRouter.Instance.route(ActionNames.CartView.getName(), request, response);
    }

    public void my(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(SessionParameters.errors.getName(), CurrentSession.Instance.getErrors());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.my_order.getName());
        dispatcher.forward(request, response);
    }

    public void history(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(SessionParameters.errors.getName(), CurrentSession.Instance.getErrors());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.order_history.getName());
        dispatcher.forward(request, response);
    }
}
