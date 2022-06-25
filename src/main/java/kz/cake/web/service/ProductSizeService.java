package kz.cake.web.service;

import kz.cake.web.entity.ProductSize;
import kz.cake.web.repository.ProductSizeRepository;
import kz.cake.web.service.base.DictionaryService;

public class ProductSizeService extends DictionaryService<ProductSize, ProductSizeRepository> {
    public ProductSizeService() {
        this.repository = new ProductSizeRepository();
        this.supplier = () -> new ProductSize();
    }

    @Override
    public String cacheKey() {
        return "ProductSize";
    }

    @Override
    public String cacheKeyWithLocal() {
        return "ProductSizeWithLocal";
    }
}
