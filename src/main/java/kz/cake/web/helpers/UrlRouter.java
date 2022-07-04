package kz.cake.web.helpers;

import kz.cake.web.controller.*;
import kz.cake.web.controller.base.BaseController;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.model.ValidationErrorDto;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlRouter {
    public static final UrlRouter Instance = new UrlRouter();
    public static final Map<String, BaseController> routes = new HashMap<>();

    static {
        routes.put("Auth", new AuthController());
        routes.put("City", new CityController());
        routes.put("User", new UserController());
        routes.put("Cart", new CartController());
        routes.put("Local", new LocalController());
        routes.put("Order", new OrderController());
        routes.put("Product", new ProductController());
        routes.put("Size", new ProductSizeController());
        routes.put("Photo", new ProductphotoController());
        routes.put("Languages", new LanguagesController());
        routes.put("Comment", new ProductCommentController());
        routes.put("Category", new ProductCategoryController());
    }

    public void route(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String url = request.getRequestURI();
            String[] path = url.split("/");
            String[] actions = StringUtils.splitByUppercase(path[path.length - 1]);
            String controllerName = actions[0];

            BaseController controller = routes.get(controllerName);
            controller.execute(actions[1], request, response);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            List<ValidationErrorDto> errorList = Arrays.asList(new ValidationErrorDto(sw.toString()));
            request.setAttribute(SessionParameters.errors.getName(), errorList);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.error.getName());
            dispatcher.forward(request, response);
        }
    }

    public void route(String actionName, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String[] actions = StringUtils.splitByUppercase(actionName);
            String controllerName = actions[0];

            BaseController controller = routes.get(controllerName);
            controller.execute(actions[1], request, response);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            List<ValidationErrorDto> errorList = Arrays.asList(new ValidationErrorDto(sw.toString()));
            request.setAttribute(SessionParameters.errors.getName(), errorList);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.error.getName());
            dispatcher.forward(request, response);
        }
    }
}
