package kz.cake.web.repository;

import kz.cake.web.entity.ProductCategory;
import kz.cake.web.repository.base.BaseRepository;

public class ProductCategoryRepository extends BaseRepository<ProductCategory> {
    public ProductCategoryRepository() {
        supplier = () -> new ProductCategory();
    }
}
