package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.RequestParameters;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.service.LocalService;
import kz.cake.web.service.ProductSizeService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ProductSizeController extends BaseController {
    protected final LocalService localService;
    protected final ProductSizeService productSizeService;

    public ProductSizeController() {
        localService = new LocalService();
        productSizeService = new ProductSizeService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(SessionParameters.productSizes.getName(), productSizeService.getDictionaryWithLocal());
        request.setAttribute(SessionParameters.locals.getName(), localService.getAllByLanguage());
        request.setAttribute(SessionParameters.errors.getName(), CurrentSession.Instance.getErrors());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.product_size.getName());
        dispatcher.forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        String code = request.getParameter(RequestParameters.code.getName());

        DictionaryDto dictionary = new DictionaryDto();
        dictionary.setCode(code);
        dictionary.setActive(true);

        productSizeService.save(dictionary);
        list(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        String code = request.getParameter(RequestParameters.code.getName());

        DictionaryDto dictionary = productSizeService.getById(id);
        dictionary.setCode(code);
        productSizeService.save(dictionary);

        list(request, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CustomValidationException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        DictionaryDto dictionary = productSizeService.getById(id);
        productSizeService.delete(dictionary);

        list(request, response);
    }
}
