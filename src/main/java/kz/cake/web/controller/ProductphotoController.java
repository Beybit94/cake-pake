package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.entity.ProductPhoto;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.constants.RequestParameters;
import kz.cake.web.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductphotoController extends BaseController {
    private final CityService cityService;
    private final ProductService productService;
    private final ProductSizeService productSizeService;
    private final ProductPhotoService productPhotoService;
    private final ProductCategoryService productCategoryService;

    public ProductphotoController() {
        cityService = new CityService();
        productService = new ProductService();
        productSizeService = new ProductSizeService();
        productPhotoService = new ProductPhotoService();
        productCategoryService = new ProductCategoryService();
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws CustomValidationException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        ProductPhoto productPhoto = productPhotoService.read(id);
        productPhotoService.delete(productPhoto);
    }
}
