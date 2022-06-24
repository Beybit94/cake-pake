package kz.cake.web.filters;


import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.service.LanguagesService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LocalFilter implements Filter {
    private static final String code = "code";
    private final LanguagesService languagesService = new LanguagesService();
    private String defaultLanguage;

    @Override
    public void init(FilterConfig filterConfig) {
        defaultLanguage = filterConfig.getInitParameter(code);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpServletRequest.getSession(true);

        String language = (String) session.getAttribute(SessionParameters.language.getName());
        if (language == null) {
            session.setAttribute(SessionParameters.language.getName(), defaultLanguage);
            languagesService.findByCode(defaultLanguage).ifPresent(l -> {
                session.setAttribute(SessionParameters.languageId.getName(), l.getId());
                CurrentSession.Instance.setCurrentLanguageId(l.getId());
            });
        } else if (!defaultLanguage.equals(language)) {
            session.setAttribute(SessionParameters.language.getName(), language);
            languagesService.findByCode(language).ifPresent(l -> {
                session.setAttribute(SessionParameters.languageId.getName(), l.getId());
                CurrentSession.Instance.setCurrentLanguageId(l.getId());
            });
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
