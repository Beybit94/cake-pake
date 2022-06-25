package kz.cake.web.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseController {
    public void execute(String action, HttpServletRequest request, HttpServletResponse response) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = this.getClass().getMethod(action.toLowerCase(), HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);
        method.invoke(this, request, response);
    }
}
