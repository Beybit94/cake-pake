package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.entity.Product;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.RequestParameters;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.model.ProductFilterDto;
import kz.cake.web.model.PhotoDto;
import kz.cake.web.service.CityService;
import kz.cake.web.service.ProductCategoryService;
import kz.cake.web.service.ProductService;
import kz.cake.web.service.ProductSizeService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductController extends BaseController {
    private final CityService cityService;
    private final ProductService productService;
    private final ProductSizeService productSizeService;
    private final ProductCategoryService productCategoryService;

    public ProductController() {
        cityService = new CityService();
        productService = new ProductService();
        productSizeService = new ProductSizeService();
        productCategoryService = new ProductCategoryService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        ProductFilterDto productFilterDto = session.getAttribute(SessionParameters.filter.getName()) != null ? (ProductFilterDto) session.getAttribute(SessionParameters.filter.getName()) : new ProductFilterDto();

        if (request.getParameter(RequestParameters.fromPrice.getName()) != null && !request.getParameter(RequestParameters.fromPrice.getName()).isEmpty()) {
            productFilterDto.setFromPrice(new BigDecimal(request.getParameter(RequestParameters.fromPrice.getName())));
        } else if (productFilterDto.getFromPrice() == null) {
            productFilterDto.setFromPrice(new BigDecimal(2000));
        }

        if (request.getParameter(RequestParameters.toPrice.getName()) != null && !request.getParameter(RequestParameters.toPrice.getName()).isEmpty()) {
            productFilterDto.setToPrice(new BigDecimal(request.getParameter(RequestParameters.toPrice.getName())));
        } else if (productFilterDto.getToPrice() == null) {
            productFilterDto.setToPrice(new BigDecimal(12000));
        }

        if (request.getParameter(RequestParameters.city.getName()) != null && !request.getParameter(RequestParameters.city.getName()).isEmpty()) {
            productFilterDto.setCityId(Long.parseLong(request.getParameter(RequestParameters.city.getName())));
        } else {
            productFilterDto.setCityId(null);
        }

        if (request.getParameter(RequestParameters.productSize.getName()) != null && !request.getParameter(RequestParameters.productSize.getName()).isEmpty()) {
            productFilterDto.setSizeId(Long.parseLong(request.getParameter(RequestParameters.productSize.getName())));
        } else {
            productFilterDto.setSizeId(null);
        }

        if (request.getParameter(RequestParameters.productCategory.getName()) != null && !request.getParameter(RequestParameters.productCategory.getName()).isEmpty()) {
            productFilterDto.setCategoryId(Long.parseLong(request.getParameter(RequestParameters.productCategory.getName())));
        } else {
            productFilterDto.setCategoryId(null);
        }

        session.setAttribute(SessionParameters.filter.getName(), productFilterDto);
        request.setAttribute(SessionParameters.filter.getName(), productFilterDto);
        request.setAttribute(SessionParameters.products.getName(), productService.find(productFilterDto));
        request.setAttribute(SessionParameters.cities.getName(), cityService.getDictionaryWithLocal());
        request.setAttribute(SessionParameters.productSizes.getName(), productSizeService.getDictionaryWithLocal());
        request.setAttribute(SessionParameters.productCategories.getName(), productCategoryService.getSelectedOptionGroup());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
        dispatcher.forward(request, response);
    }

    public void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        request.setAttribute(SessionParameters.item.getName(), productService.getById(id));
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.detail_product.getName());
        dispatcher.forward(request, response);
    }

    public void my(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductFilterDto productFilterDto = new ProductFilterDto();
        productFilterDto.setUserId(CurrentSession.Instance.getCurrentUser().getUserId());

        request.setAttribute(SessionParameters.products.getName(), productService.find(productFilterDto));
        request.setAttribute(SessionParameters.cities.getName(), cityService.getDictionaryWithLocal());
        request.setAttribute(SessionParameters.productSizes.getName(), productSizeService.getDictionaryWithLocal());
        request.setAttribute(SessionParameters.productCategories.getName(), productCategoryService.getSelectedOptionGroup());
        request.setAttribute(SessionParameters.errors.getName(), CurrentSession.Instance.getErrors());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.my_product.getName());
        dispatcher.forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CustomValidationException, SQLException, IllegalAccessException {
        Product product = new Product.Builder()
                .name(request.getParameter(RequestParameters.productName.getName()))
                .description(request.getParameter(RequestParameters.description.getName()))
                .price(new BigDecimal(request.getParameter(RequestParameters.price.getName())))
                .userId(CurrentSession.Instance.getCurrentUser().getUserId())
                .cityId(Long.parseLong(request.getParameter(RequestParameters.city.getName())))
                .sizeId(Long.parseLong(request.getParameter(RequestParameters.productSize.getName())))
                .categoryId(Long.parseLong(request.getParameter(RequestParameters.productCategory.getName())))
                .active(true)
                .build();

        List<PhotoDto> photos = new ArrayList<>();
        List<Part> fileParts = request.getParts().stream().filter(part -> "file" .equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
        for (Part filePart : fileParts) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            InputStream fileContent = filePart.getInputStream();
            PhotoDto photo = new PhotoDto();
            photo.setName(fileName);
            photo.setFileContent(fileContent);
            photos.add(photo);
        }
        productService.save(product, photos);
        my(request, response);
    }

    public void read(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));

        request.setAttribute(SessionParameters.item.getName(), productService.getById(id));
        request.setAttribute(SessionParameters.cities.getName(), cityService.getDictionaryWithLocal());
        request.setAttribute(SessionParameters.productSizes.getName(), productSizeService.getDictionaryWithLocal());
        request.setAttribute(SessionParameters.productCategories.getName(), productCategoryService.getSelectedOptionGroup());
        request.setAttribute(SessionParameters.errors.getName(), CurrentSession.Instance.getErrors());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.edit_product.getName());
        dispatcher.forward(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CustomValidationException, SQLException, IllegalAccessException {
        Product product = new Product.Builder()
                .id(Long.parseLong(request.getParameter(RequestParameters.id.getName())))
                .name(request.getParameter(RequestParameters.productName.getName()))
                .description(request.getParameter(RequestParameters.description.getName()))
                .price(new BigDecimal(request.getParameter(RequestParameters.price.getName())))
                .userId(CurrentSession.Instance.getCurrentUser().getUserId())
                .cityId(Long.parseLong(request.getParameter(RequestParameters.city.getName())))
                .sizeId(Long.parseLong(request.getParameter(RequestParameters.productSize.getName())))
                .categoryId(Long.parseLong(request.getParameter(RequestParameters.productCategory.getName())))
                .active(true)
                .build();

        List<PhotoDto> photos = new ArrayList<>();
        List<Part> fileParts = request.getParts().stream().filter(part -> "file" .equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
        for (Part filePart : fileParts) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            InputStream fileContent = filePart.getInputStream();
            PhotoDto photo = new PhotoDto();
            photo.setName(fileName);
            photo.setFileContent(fileContent);
            photos.add(photo);
        }
        productService.save(product, photos);
        my(request, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CustomValidationException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        Product product = productService.read(id);
        productService.delete(product);

        my(request, response);
    }
}
