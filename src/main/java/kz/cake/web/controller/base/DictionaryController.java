package kz.cake.web.controller.base;

import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.service.LocalService;
import kz.cake.web.service.base.DictionaryService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public abstract class DictionaryController<T extends DictionaryService> extends BaseController {
    protected final LocalService localService;
    protected T service;

    public DictionaryController() {
        localService = new LocalService();
    }

    public abstract Map<String, SessionParameters> getSessionParameters();

    public abstract Map<String, PageNames> getPageNames();

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute(getSessionParameters().get("list").getName(), service.getDictionaryWithLocal());
        session.setAttribute(SessionParameters.locals.getName(), localService.getAllByLanguage());
        RequestDispatcher dispatcher = request.getRequestDispatcher(getPageNames().get("list").getName());
        dispatcher.forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");

        DictionaryDto dictionary = new DictionaryDto();
        dictionary.setCode(code);
        dictionary.setActive(true);

        service.save(dictionary);
        list(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String code = request.getParameter("code");

        DictionaryDto dictionary = service.getById(id);
        dictionary.setCode(code);
        service.save(dictionary);

        list(request, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        DictionaryDto dictionary = service.getById(id);
        service.delete(dictionary);

        list(request, response);
    }
}
