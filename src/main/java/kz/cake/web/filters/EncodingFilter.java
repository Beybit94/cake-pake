package kz.cake.web.filters;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    private static final String code = "code";
    private static final String type = "type";

    private static final String defaultCode= "UTF-8";
    private static final String defaultType = "text/html";

    private String encoding;
    private String contentType;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter(code);
        contentType = filterConfig.getInitParameter(type);
        if (encoding == null || contentType == null){
            encoding = defaultCode;
            contentType = defaultType;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setContentType(contentType);
        servletRequest.setCharacterEncoding(encoding);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
