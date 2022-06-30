package kz.cake.web.service;

import kz.cake.web.entity.ProductCategory;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CacheProvider;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.ProductCategoryDto;
import kz.cake.web.model.SelectOptionGroupDto;
import kz.cake.web.repository.ProductCategoryRepository;
import kz.cake.web.service.base.DictionaryService;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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
    public ProductCategory save(DictionaryDto dictionary) throws SQLException, IllegalAccessException {
        CacheProvider.remove(cacheKey());
        CacheProvider.remove(cacheKeyWithLocal());

        ProductCategoryDto productCategoryDto = (ProductCategoryDto) dictionary;
        return super.save(new ProductCategory.Builder()
                .id(productCategoryDto.getId())
                .parent(productCategoryDto.getParent())
                .code(productCategoryDto.getCode())
                .active(productCategoryDto.isActive())
                .build());
    }

    @Override
    public void delete(DictionaryDto dictionary) throws CustomValidationException {
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

    public List<SelectOptionGroupDto> getSelectedOptionGroup() {
        return getDictionaryWithLocal()
                .stream()
                .map(m -> (ProductCategoryDto) m)
                .filter(m -> m.getParent() == null)
                .map(m -> mapToSelect(m))
                .collect(Collectors.toList());

    }

    public List<ProductCategoryDto> findByParent(Long id) {
        return getDictionaryWithLocal().stream()
                .filter(m -> {
                    ProductCategoryDto productCategoryDto = (ProductCategoryDto) m;
                    return productCategoryDto.getParent() != null && productCategoryDto.getParent().equals(id);
                })
                .map(m -> (ProductCategoryDto) m)
                .collect(Collectors.toList());
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

    protected SelectOptionGroupDto mapToSelect(ProductCategoryDto m) {
        SelectOptionGroupDto selectOptionGroupDto = new SelectOptionGroupDto();
        selectOptionGroupDto.setId(m.getId());
        selectOptionGroupDto.setText(m.getText());
        selectOptionGroupDto.setChildren(findByParent(m.getId()).stream()
                .map(c -> {
                    SelectOptionGroupDto selectOptionGroupDto1 = new SelectOptionGroupDto();
                    selectOptionGroupDto1.setId(c.getId());
                    selectOptionGroupDto1.setText(c.getText());
                    return selectOptionGroupDto1;
                })
                .collect(Collectors.toList()));
        return selectOptionGroupDto;
    }
}
