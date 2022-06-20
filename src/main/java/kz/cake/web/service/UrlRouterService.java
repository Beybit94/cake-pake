package kz.cake.web.service;

import kz.cake.web.exceptions.ServiceNotFoundException;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.helpers.constants.PageNames;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.reflections.Reflections;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class UrlRouterService {
    public static final UrlRouterService Instance = new UrlRouterService();
    private final Logger logger = LogManager.getLogger(UrlRouterService.class);

    public void route(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String url = request.getRequestURI();
            String[] path = url.split("/");
            String[] actions = StringUtils.splitByUppercase(path[path.length - 1]);
            String serviceName = actions[0] + "Service";

            Reflections reflections = new Reflections("kz.cake.web.service");
            Optional<Class<? extends BaseService>> clazz = reflections.getSubTypesOf(BaseService.class).stream()
                    .filter(s -> s.getSimpleName().equals(serviceName))
                    .findFirst();

            if (!clazz.isPresent()) throw new ServiceNotFoundException();

            BaseService service = clazz.get().getDeclaredConstructor().newInstance();
            service.execute(actions[1], request, response);
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
            dispatcher.forward(request, response);
        }
    }
}
