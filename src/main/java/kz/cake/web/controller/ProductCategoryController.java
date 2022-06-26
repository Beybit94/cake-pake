package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.service.LocalService;
import kz.cake.web.service.ProductCategoryService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ProductCategoryController extends BaseController {
    protected final LocalService localService;
    protected final ProductCategoryService productCategoryService;

    public ProductCategoryController() {
        localService = new LocalService();
        productCategoryService = new ProductCategoryService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParameters.productCategories.getName(), productCategoryService.getDictionaryWithLocal());
        session.setAttribute(SessionParameters.locals.getName(), localService.getAllByLanguage());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.product_category.getName());
        dispatcher.forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        Long parent = request.getParameter("parent") == null || request.getParameter("parent").isEmpty() ? null : Long.parseLong(request.getParameter("parent"));

        DictionaryDto dictionary = new DictionaryDto();
        dictionary.setCode(code);
        dictionary.setParent(parent);
        dictionary.setActive(true);

        productCategoryService.save(dictionary);
        list(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String code = request.getParameter("code");
        Long parent = request.getParameter("parent") == null || request.getParameter("parent").isEmpty() ? null : Long.parseLong(request.getParameter("parent"));

        DictionaryDto dictionary = productCategoryService.getById(id);
        dictionary.setCode(code);
        dictionary.setParent(parent);
        productCategoryService.save(dictionary);

        list(request, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        DictionaryDto dictionary = productCategoryService.getById(id);
        productCategoryService.delete(dictionary);

        list(request, response);
    }
}
