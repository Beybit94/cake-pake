package kz.cake.web.service;

import kz.cake.web.entity.ProductCategory;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CacheProvider;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.ProductCategoryDto;
import kz.cake.web.model.ProductDto;
import kz.cake.web.model.ProductFilterDto;
import kz.cake.web.repository.ProductCategoryRepository;
import kz.cake.web.service.base.DictionaryService;

import java.sql.SQLException;
import java.util.List;

public class ProductCategoryService extends DictionaryService<ProductCategory, ProductCategoryRepository> {
    private final ProductService productService;

    public ProductCategoryService() {
        super();
        this.repository = new ProductCategoryRepository();
        this.supplier = () -> new ProductCategory();
        productService = new ProductService();
    }

    @Override
    public String cacheKey() {
        return "ProductCategory";
    }

    @Override
    public String cacheKeyWithLocal() {
        return "ProductCategoryWithLocal";
    }

    @Override
    public void save(DictionaryDto dictionary) throws SQLException, IllegalAccessException {
        ProductCategoryDto productCategoryDto = (ProductCategoryDto) dictionary;
        super.save(new ProductCategory.Builder()
                .id(productCategoryDto.getId())
                .parent(productCategoryDto.getParent())
                .code(productCategoryDto.getCode())
                .active(productCategoryDto.isActive())
                .build());

        CacheProvider.remove(cacheKey());
        CacheProvider.remove(cacheKeyWithLocal());
    }

    @Override
    public void delete(DictionaryDto dictionary) throws CustomValidationException {
        List<ProductDto> find = productService.find(new ProductFilterDto());
        if (!find.isEmpty()) {
            throw new CustomValidationException("error.activeRecord", ActionNames.ProductsizeList);
        }

        ProductCategoryDto productCategoryDto = (ProductCategoryDto) dictionary;
        super.delete(new ProductCategory.Builder()
                .id(productCategoryDto.getId())
                .parent(productCategoryDto.getParent())
                .code(productCategoryDto.getCode())
                .active(productCategoryDto.isActive())
                .build());

        CacheProvider.remove(cacheKey());
        CacheProvider.remove(cacheKeyWithLocal());
    }

    @Override
    protected ProductCategoryDto mapWithLocal(ProductCategory m) {
        ProductCategoryDto dictionaryDto = new ProductCategoryDto(m.getId(), m.getParent(), m.getCode(), m.isActive());
        localService.findByCode(m.getCode()).ifPresent(p -> {
            dictionaryDto.setText(p.getMessage());
        });

        if (m.getParent() != null) {
            DictionaryDto parent = getById(m.getParent());
            dictionaryDto.setParentCode(parent.getCode());
            localService.findByCode(parent.getCode()).ifPresent(p -> {
                dictionaryDto.setParentText(p.getMessage());
            });
        }
        return dictionaryDto;
    }

    @Override
    protected ProductCategoryDto map(ProductCategory m) {
        ProductCategoryDto dictionaryDto = new ProductCategoryDto(m.getId(), m.getParent(), m.getCode(), m.isActive());
        if (m.getParent() != null) {
            DictionaryDto parent = getById(m.getParent());
            dictionaryDto.setParentCode(parent.getCode());
        }
        return dictionaryDto;
    }
}
