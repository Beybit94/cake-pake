package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.helpers.UrlRouter;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.service.LanguagesService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LanguagesController extends BaseController {
    private final LanguagesService languagesService;

    public LanguagesController() {
        languagesService = new LanguagesService();
    }

    public void change(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String redirect = request.getParameter("redirect");

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParameters.language.getName(), code);
        if (redirect == null || redirect.isEmpty()) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
            dispatcher.forward(request, response);
        } else {
            UrlRouter.Instance.route(redirect, request, response);
        }
    }
}
