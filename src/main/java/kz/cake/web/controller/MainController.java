package kz.cake.web.controller;

import kz.cake.web.helpers.UrlRouter;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.model.ValidationErrorDto;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class MainController extends HttpServlet {
    public MainController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UrlRouter.Instance.route(req, resp);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            List<ValidationErrorDto> errorList = Arrays.asList(new ValidationErrorDto(sw.toString()));
            req.setAttribute(SessionParameters.errors.getName(), errorList);
            RequestDispatcher dispatcher = req.getRequestDispatcher(PageNames.error.getName());
            dispatcher.forward(req, resp);
        }
    }
}
