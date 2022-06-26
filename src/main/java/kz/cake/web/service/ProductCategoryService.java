package kz.cake.web.service;

import kz.cake.web.entity.Local;
import kz.cake.web.entity.ProductCategory;
import kz.cake.web.helpers.CacheProvider;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.repository.ProductCategoryRepository;
import kz.cake.web.service.base.DictionaryService;

import java.util.Optional;

public class ProductCategoryService extends DictionaryService<ProductCategory, ProductCategoryRepository> {
    public ProductCategoryService() {
        super();
        this.repository = new ProductCategoryRepository();
        this.supplier = () -> new ProductCategory();
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
    public void save(DictionaryDto dictionary) {
        super.save(new ProductCategory.Builder()
                .id(dictionary.getId())
                .parent(dictionary.getParent())
                .code(dictionary.getCode())
                .active(dictionary.isActive())
                .build());

        CacheProvider.remove(cacheKey());
        CacheProvider.remove(cacheKeyWithLocal());
    }

    @Override
    public void delete(DictionaryDto dictionary) {
        super.delete(new ProductCategory.Builder()
                .id(dictionary.getId())
                .parent(dictionary.getParent())
                .code(dictionary.getCode())
                .active(dictionary.isActive())
                .build());

        CacheProvider.remove(cacheKey());
        CacheProvider.remove(cacheKeyWithLocal());
    }

    @Override
    protected DictionaryDto mapWithLocal(ProductCategory m) {
        DictionaryDto dictionaryDto = new DictionaryDto(m.getId(), m.getParent(), m.getCode(), m.isActive());
        localService.getByCode(m.getCode()).ifPresent(p->{
            dictionaryDto.setText(p.getMessage());
        });

        if(m.getParent()!=null){
            DictionaryDto parent = getById(m.getParent());
            dictionaryDto.setParentCode(parent.getCode());
            localService.getByCode(parent.getCode()).ifPresent(p->{
                dictionaryDto.setParentText(p.getMessage());
            });
        }
        return dictionaryDto;
    }

    @Override
    protected DictionaryDto map(ProductCategory m) {
        DictionaryDto dictionaryDto = new DictionaryDto(m.getId(), m.getParent(), m.getCode(), m.isActive());
        if(m.getParent()!=null){
            DictionaryDto parent = getById(m.getParent());
            dictionaryDto.setParentCode(parent.getCode());
        }
        return dictionaryDto;
    }
}
