package kz.cake.web.filters;

import javax.servlet.*;
import java.io.IOException;
import java.util.TimeZone;

public class TimezoneFilter implements Filter {
    private static final String code = "code";
    private static final String defaultTimezone = "UTC";

    private String timeZone;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        timeZone = filterConfig.getInitParameter(code);

        if (timeZone == null){
            timeZone = defaultTimezone;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String currentTimeZone = TimeZone.getDefault().getID();

        if (!currentTimeZone.equals(timeZone)) {
            TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
