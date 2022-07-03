package kz.cake.web.controller.base;

import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.UrlRouter;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.model.ValidationErrorDto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public abstract class BaseController {
    public void execute(String action, HttpServletRequest request, HttpServletResponse response) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ServletException, IOException {
        try {
            Method method = this.getClass().getMethod(action.toLowerCase(), HttpServletRequest.class, HttpServletResponse.class);
            method.setAccessible(true);
            method.invoke(this, request, response);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof CustomValidationException) {
                CustomValidationException exception = (CustomValidationException) e.getCause();
                List<ValidationErrorDto> errorList = Arrays.asList(new ValidationErrorDto(exception.getMessageCode()));
                CurrentSession.Instance.setErrors(errorList);
                ActionNames redirect = exception.getRedirect();
                UrlRouter.Instance.route(redirect.getName(), request, response);
            } else {
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
