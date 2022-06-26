package kz.cake.web.repository;

import kz.cake.web.entity.ProductCategory;
import kz.cake.web.repository.base.DictionaryRepository;

public class ProductCategoryRepository extends DictionaryRepository<ProductCategory> {
    public ProductCategoryRepository() {
        supplier = () -> new ProductCategory();
    }
}
