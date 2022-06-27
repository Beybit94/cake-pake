package kz.cake.web.service;

import kz.cake.web.entity.Local;
import kz.cake.web.entity.ProductSize;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.ProductDto;
import kz.cake.web.model.ProductFilterDto;
import kz.cake.web.repository.ProductSizeRepository;
import kz.cake.web.service.base.DictionaryService;

import java.util.List;
import java.util.Optional;

public class ProductSizeService extends DictionaryService<ProductSize, ProductSizeRepository> {
    private final ProductService productService;

    public ProductSizeService() {
        super();
        this.repository = new ProductSizeRepository();
        this.supplier = () -> new ProductSize();
        productService = new ProductService();
    }

    @Override
    public String cacheKey() {
        return "ProductSize";
    }

    @Override
    public String cacheKeyWithLocal() {
        return "ProductSizeWithLocal";
    }

    @Override
    public void delete(DictionaryDto dictionary) throws CustomValidationException {
        List<ProductDto> find = productService.find(new ProductFilterDto());
        if (!find.isEmpty()) {
            throw new CustomValidationException("error.activeRecord", ActionNames.ProductsizeList);
        }
        super.delete(dictionary);
    }

    @Override
    protected DictionaryDto mapWithLocal(ProductSize m) {
        DictionaryDto dictionaryDto = new DictionaryDto(m.getId(), m.getCode(), m.isActive());
        Optional<Local> local = localService.findByCode(m.getCode());
        if (local.isPresent()) {
            dictionaryDto.setText(local.get().getMessage());
        }
        return dictionaryDto;
    }

    @Override
    protected DictionaryDto map(ProductSize m) {
        return new DictionaryDto(m.getId(), m.getCode(), m.isActive());
    }
}
