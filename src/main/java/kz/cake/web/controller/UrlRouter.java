package kz.cake.web.controller;

import kz.cake.web.exceptions.ControllerNotFoundException;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.helpers.constants.PageNames;
import org.reflections.Reflections;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class UrlRouter {
    public static final UrlRouter Instance = new UrlRouter();

    public void route(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String url = request.getRequestURI();
            String[] path = url.split("/");
            String[] actions = StringUtils.splitByUppercase(path[path.length - 1]);
            String controllerName = actions[0] + "Controller";

            Reflections reflections = new Reflections("kz.cake.web.controller");
            Optional<Class<? extends BaseController>> clazz = reflections.getSubTypesOf(BaseController.class).stream()
                    .filter(s -> s.getSimpleName().equals(controllerName))
                    .findFirst();

            if (!clazz.isPresent()) throw new ControllerNotFoundException();

            BaseController controller = clazz.get().getDeclaredConstructor().newInstance();
            controller.execute(actions[1], request, response);
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
            dispatcher.forward(request, response);
        }
    }

    public void route(String actionName,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String[] actions = StringUtils.splitByUppercase(actionName);
            String controllerName = actions[0] + "Controller";

            Reflections reflections = new Reflections("kz.cake.web.controller");
            Optional<Class<? extends BaseController>> clazz = reflections.getSubTypesOf(BaseController.class).stream()
                    .filter(s -> s.getSimpleName().equals(controllerName))
                    .findFirst();

            if (!clazz.isPresent()) throw new ControllerNotFoundException();

            BaseController controller = clazz.get().getDeclaredConstructor().newInstance();
            controller.execute(actions[1], request, response);
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
            dispatcher.forward(request, response);
        }
    }
}
