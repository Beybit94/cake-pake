package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderController extends BaseController {
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
