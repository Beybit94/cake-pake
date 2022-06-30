package kz.cake.web.repository;

import kz.cake.web.entity.Product;
import kz.cake.web.entity.ProductCategory;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.repository.base.DictionaryRepository;

import java.util.Optional;

public class ProductCategoryRepository extends DictionaryRepository<ProductCategory> {
    private final ProductRepository productRepository;

    public ProductCategoryRepository() {
        supplier = () -> new ProductCategory();
        productRepository = new ProductRepository();
    }

    @Override
    public void delete(ProductCategory entity) throws CustomValidationException {
        Optional<Product> find = productRepository.getAll().stream().filter(m -> m.getCategoryId().equals(entity.getId())).findAny();
        if (find.isPresent()) {
            throw new CustomValidationException("error.activeRecord", ActionNames.CityList);
        }

        super.delete(entity);
    }
}
