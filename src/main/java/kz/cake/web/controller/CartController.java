package kz.cake.web.controller;

import com.google.gson.Gson;
import kz.cake.web.controller.base.BaseController;
import kz.cake.web.entity.OrderDetail;
import kz.cake.web.entity.Product;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.constants.LocaleCodes;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.RequestParameters;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.OrderDetailDto;
import kz.cake.web.model.OrderDto;
import kz.cake.web.model.ProductDto;
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

public class CartController extends BaseController {
    private final OrderService orderService;
    private final ProductService productService;

    private final OrderDetailService orderDetailService;
    private final OrderStatusService orderStatusService;

    public CartController() {
        orderService = new OrderService();
        productService = new ProductService();
        orderDetailService = new OrderDetailService();
        orderStatusService = new OrderStatusService();
    }

    public void view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.shopping_cart.getName());
        dispatcher.forward(request, response);
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDto draft = orderService.getDraft().orElse(new OrderDto());
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.print(new Gson().toJson(draft));
        out.flush();
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, IllegalAccessException, CustomValidationException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        ProductDto product = productService.getById(id);

        DictionaryDto status = orderStatusService.findByCode(LocaleCodes.statusDraft.getName()).get();
        OrderDto draft = orderService.getDraft()
                .orElse(new OrderDto(status, Arrays.asList(new OrderDetailDto(product))));

        if (draft.getOrderDetail().stream().filter(m -> m.getProduct().getId().equals(product.getId())).findAny().isPresent()) {
            draft.getOrderDetail().stream().forEach(m -> {
                if (m.getProduct().getId().equals(product.getId())) {
                    m.setQuantity(m.getQuantity() + 1);
                }
            });
        } else {
            OrderDetailDto newOrderDetail = new OrderDetailDto(product);
            newOrderDetail.setQuantity(1);
            draft.getOrderDetail().add(newOrderDetail);
        }

        orderService.save(draft);

        draft = orderService.getDraft().orElse(new OrderDto());
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParameters.orderDraft.getName(), draft);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.print(new Gson().toJson(draft));
        out.flush();
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws SQLException, IllegalAccessException, IOException, CustomValidationException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        Product product = productService.read(id);

        OrderDto draft = orderService.getDraft().orElse(new OrderDto());
        if (draft.getOrderDetail().stream().filter(m -> m.getProduct().getId().equals(product.getId())).findAny().isPresent()) {
            draft.getOrderDetail().stream().forEach(m -> {
                if (m.getProduct().getId().equals(product.getId())) {
                    m.setQuantity(m.getQuantity() - 1);
                }
            });
        }

        orderService.save(draft);

        draft = orderService.getDraft().orElse(new OrderDto());
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParameters.orderDraft.getName(), orderService.getDraft().orElse(new OrderDto()));

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.print(new Gson().toJson(draft));
        out.flush();
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws CustomValidationException, IOException, ServletException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        OrderDetail orderDetail = orderDetailService.read(id);
        orderDetailService.delete(orderDetail);

        OrderDto draft = orderService.getDraft().orElse(new OrderDto());
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParameters.orderDraft.getName(), draft);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.print(new Gson().toJson(draft));
        out.flush();
    }
}
