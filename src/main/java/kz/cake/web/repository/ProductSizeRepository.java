package kz.cake.web.repository;

import kz.cake.web.entity.ProductSize;
import kz.cake.web.repository.base.BaseRepository;
import kz.cake.web.repository.base.DictionaryRepository;

public class ProductSizeRepository extends DictionaryRepository<ProductSize> {
    public ProductSizeRepository() {
        supplier = () -> new ProductSize();
    }
}
