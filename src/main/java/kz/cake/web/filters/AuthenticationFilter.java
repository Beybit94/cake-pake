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
        CurrentUserDto currentUser = CurrentSession.Instance.getCurrentUser();
        if (currentUser != null) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            String url = request.getRequestURI();
            String[] path = url.split("/");

            String action = path[path.length - 1];
            if (action.length() <= 0) filterChain.doFilter(servletRequest, servletResponse);
            if(action.contains(".jsp")) filterChain.doFilter(servletRequest, servletResponse);

            RequestDispatcher dispatcher = null;
            if (currentUser.getRoles().contains("user") && !ActionNames.userPermissions.contains(action)) {
                dispatcher = request.getRequestDispatcher(PageNames.access_denied.getName());
            } else if (currentUser.getRoles().contains("manager") && !ActionNames.managerPermissions.contains(action)) {
                dispatcher = request.getRequestDispatcher(PageNames.access_denied.getName());
            }

            if(dispatcher != null){
                dispatcher.forward(request, response);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
