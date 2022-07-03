package kz.cake.web.service;

import kz.cake.web.entity.Product;
import kz.cake.web.entity.ProductPhoto;
import kz.cake.web.entity.User;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.model.*;
import kz.cake.web.repository.ProductRepository;
import kz.cake.web.service.base.BaseService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductService extends BaseService<Product, ProductRepository> {
    private final UserService userService;
    private final CityService cityService;
    private final ProductSizeService productSizeService;
    private final ProductPhotoService productPhotoService;
    private final ProductCommentService productCommentService;
    private final ProductCategoryService productCategoryService;

    public ProductService() {
        this.repository = new ProductRepository();
        userService = new UserService();
        cityService = new CityService();
        productSizeService = new ProductSizeService();
        productPhotoService = new ProductPhotoService();
        productCommentService = new ProductCommentService();
        productCategoryService = new ProductCategoryService();
    }

    public void save(Product entity, List<PhotoDto> photos) throws SQLException, IllegalAccessException, IOException {
        Product product = super.save(entity);

        for (PhotoDto photoDto : photos) {
            productPhotoService.save(photoDto, product.getId());
        }
    }

    public ProductDto getById(Long id) {
        Product product = super.read(id);
        return map(product);
    }

    public List<ProductDto> find(ProductFilterDto productFilterDto) {
        return getAll().stream()
                .filter(filter(productFilterDto))
                .map(this::map)
                .collect(Collectors.toList());
    }

    private Predicate<Product> filter(ProductFilterDto productFilterDto) {
        return product -> (productFilterDto.getUserId() == null || product.getUserId().equals(productFilterDto.getUserId())) &&
                (productFilterDto.getCityId() == null || product.getCityId().equals(productFilterDto.getCityId())) &&
                (productFilterDto.getSizeId() == null || product.getSizeId().equals(productFilterDto.getSizeId())) &&
                (productFilterDto.getCategoryId() == null || product.getCategoryId().equals(productFilterDto.getCategoryId())) &&
                (productFilterDto.getFromPrice() == null || product.getPrice().compareTo(productFilterDto.getFromPrice()) >= 0) &&
                (productFilterDto.getToPrice() == null || product.getPrice().compareTo(productFilterDto.getToPrice()) <= 0);
    }

    private ProductDto map(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setPriceText(StringUtils.currencyFormat(product.getPrice()));
        productDto.setComments(productCommentService.getByProductId(product.getId()));

        if (product.getUserId() != null) {
            User user = userService.read(product.getUserId());
            productDto.setUser(user);
        }

        if (product.getCityId() != null) {
            DictionaryDto city = cityService.getByIdWithLocal(product.getCityId());
            productDto.setCity(city);
        }

        if (product.getSizeId() != null) {
            DictionaryDto productSize = productSizeService.getByIdWithLocal(product.getSizeId());
            productDto.setProductSize(productSize);
        }

        if (product.getCategoryId() != null) {
            CategoryDto productCategory = (CategoryDto) productCategoryService.getByIdWithLocal(product.getCategoryId());
            productDto.setProductCategory(productCategory);
        }

        for (ProductPhoto productPhoto : productPhotoService.getAllByProduct(product.getId())) {
            productDto.getPhotos().add(new PhotoDto(productPhoto.getId(), productPhoto.getImage()));
        }

        return productDto;
    }
}
