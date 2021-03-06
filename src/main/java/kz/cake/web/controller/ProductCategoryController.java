package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.RequestParameters;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.CategoryDto;
import kz.cake.web.service.LocalService;
import kz.cake.web.service.ProductCategoryService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ProductCategoryController extends BaseController {
    protected final LocalService localService;
    protected final ProductCategoryService productCategoryService;

    public ProductCategoryController() {
        localService = new LocalService();
        productCategoryService = new ProductCategoryService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(SessionParameters.productCategories.getName(), productCategoryService.getDictionaryWithLocal());
        request.setAttribute(SessionParameters.locals.getName(), localService.getAllByLanguage());
        request.setAttribute(SessionParameters.errors.getName(), CurrentSession.Instance.getErrors());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.product_category.getName());
        dispatcher.forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        String code = request.getParameter(RequestParameters.code.getName());
        Long parent = request.getParameter(RequestParameters.parent.getName()) == null || request.getParameter(RequestParameters.parent.getName()).isEmpty() ? null : Long.parseLong(request.getParameter(RequestParameters.parent.getName()));

        CategoryDto dictionary = new CategoryDto();
        dictionary.setCode(code);
        dictionary.setParent(parent);
        dictionary.setActive(true);

        productCategoryService.save(dictionary);
        list(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        String code = request.getParameter(RequestParameters.code.getName());
        Long parent = request.getParameter(RequestParameters.parent.getName()) == null || request.getParameter(RequestParameters.parent.getName()).isEmpty() ? null : Long.parseLong(request.getParameter(RequestParameters.parent.getName()));

        CategoryDto dictionary = (CategoryDto) productCategoryService.getById(id);
        dictionary.setCode(code);
        dictionary.setParent(parent);
        productCategoryService.save(dictionary);

        list(request, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CustomValidationException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        DictionaryDto dictionary = productCategoryService.getById(id);
        productCategoryService.delete(dictionary);

        list(request, response);
    }
}
