package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.service.CityService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CityController extends BaseController {
    private final CityService cityService;

    public CityController() {
        cityService = new CityService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParameters.dictionary.getName(), cityService.getDictionaryWithLocal());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.city.getName());
        dispatcher.forward(request, response);
    }
}
