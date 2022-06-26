package kz.cake.web.service;

import kz.cake.web.entity.Local;
import kz.cake.web.entity.ProductSize;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.repository.ProductSizeRepository;
import kz.cake.web.service.base.DictionaryService;

import java.util.Optional;

public class ProductSizeService extends DictionaryService<ProductSize, ProductSizeRepository> {
    public ProductSizeService() {
        super();
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

    @Override
    protected DictionaryDto mapWithLocal(ProductSize m) {
        DictionaryDto dictionaryDto = new DictionaryDto(m.getId(), m.getCode(), m.isActive());
        Optional<Local> local = localService.getByCode(m.getCode());
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
