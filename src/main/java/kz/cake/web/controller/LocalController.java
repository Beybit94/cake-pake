package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.entity.Local;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.RequestParameters;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.service.LanguagesService;
import kz.cake.web.service.LocalService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class LocalController extends BaseController {
    private final LocalService localService;
    private final LanguagesService languagesService;

    public LocalController() {
        localService = new LocalService();
        languagesService = new LanguagesService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(SessionParameters.locals.getName(), localService.getAllWithLanguage());
        request.setAttribute(SessionParameters.languages.getName(), languagesService.getAll());
        request.setAttribute(SessionParameters.errors.getName(), CurrentSession.Instance.getErrors());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.locals.getName());
        dispatcher.forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        localService.save(new Local.Builder()
                .code(request.getParameter(RequestParameters.code.getName()))
                .message(request.getParameter(RequestParameters.text.getName()))
                .languageId(Long.parseLong(request.getParameter(RequestParameters.languageId.getName())))
                .active(true)
                .build());
        list(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));

        Local local = localService.getById(id);
        local.setCode(request.getParameter(RequestParameters.code.getName()));
        local.setMessage(request.getParameter(RequestParameters.text.getName()));
        local.setLanguageId(Long.parseLong(request.getParameter(RequestParameters.languageId.getName())));
        localService.save(local);

        list(request, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CustomValidationException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));

        Local local = localService.getById(id);
        localService.delete(local);

        list(request, response);
    }
}
