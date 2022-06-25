package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.entity.Local;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.service.LanguagesService;
import kz.cake.web.service.LocalService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LocalController extends BaseController {
    private final LocalService localService;
    private final LanguagesService languagesService;

    public LocalController() {
        localService = new LocalService();
        languagesService = new LanguagesService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParameters.locals.getName(), localService.getAllWithLanguage());
        session.setAttribute(SessionParameters.languages.getName(), languagesService.getAll());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.locals.getName());
        dispatcher.forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        localService.save(new Local.Builder()
                .code(request.getParameter("code"))
                .message(request.getParameter("text"))
                .languageId(Long.parseLong(request.getParameter("languageId")))
                .active(true)
                .build());
        list(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));

        Local local = localService.getById(id);
        local.setCode(request.getParameter("code"));
        local.setMessage(request.getParameter("text"));
        local.setLanguageId(Long.parseLong(request.getParameter("languageId")));
        localService.save(local);

        list(request, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));

        Local local = localService.getById(id);
        localService.delete(local);

        list(request, response);
    }
}
