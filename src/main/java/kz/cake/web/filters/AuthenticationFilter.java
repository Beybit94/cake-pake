package kz.cake.web.filters;

import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.model.CurrentUserDto;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        CurrentUserDto currentUser = CurrentSession.Instance.getCurrentUser();
        RequestDispatcher dispatcher = null;
        A:
        if (currentUser != null) {
            String url = request.getRequestURI();
            String[] path = url.split("/");

            String action = path[path.length - 1];
            if (action.length() <= 0 || action.indexOf('.') > 0) {
                break A;
            } else {
                if (currentUser.getRoles().contains("user") && !ActionNames.userPermissions.contains(ActionNames.valueOf(action))) {
                    dispatcher = request.getRequestDispatcher(PageNames.access_denied.getName());
                } else if (currentUser.getRoles().contains("manager") && !ActionNames.managerPermissions.contains(ActionNames.valueOf(action))) {
                    dispatcher = request.getRequestDispatcher(PageNames.access_denied.getName());
                }
            }
        }

        if (dispatcher != null) {
            dispatcher.forward(request, response);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
