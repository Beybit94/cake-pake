package kz.cake.web.helpers;

import kz.cake.web.controller.*;
import kz.cake.web.controller.base.BaseController;
import kz.cake.web.exceptions.ControllerNotFoundException;
import kz.cake.web.helpers.constants.PageNames;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UrlRouter {
    public static final UrlRouter Instance = new UrlRouter();
    public static final Map<String, BaseController> routes = new HashMap<>();

    static {
        routes.put("Languages", new LanguagesController());
        routes.put("Local", new LocalController());
        routes.put("City", new CityController());
        routes.put("User", new UserController());
        routes.put("Productsize",new ProductSizeController());
        routes.put("Productcategory",new ProductCategoryController());
    }

    public void route(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String url = request.getRequestURI();
            String[] path = url.split("/");
            String[] actions = StringUtils.splitByUppercase(path[path.length - 1]);
            String controllerName = actions[0];

            if (!routes.containsKey(controllerName)) throw new ControllerNotFoundException();

            BaseController controller = routes.get(controllerName);
            controller.execute(actions[1], request, response);
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
            dispatcher.forward(request, response);
        }
    }

    public void route(String actionName, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String[] actions = StringUtils.splitByUppercase(actionName);
            String controllerName = actions[0];

            if (!routes.containsKey(controllerName)) throw new ControllerNotFoundException();

            BaseController controller = routes.get(controllerName);
            controller.execute(actions[1], request, response);
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
            dispatcher.forward(request, response);
        }
    }
}
