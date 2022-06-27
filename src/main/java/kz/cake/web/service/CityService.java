package kz.cake.web.service;

import kz.cake.web.entity.City;
import kz.cake.web.entity.Local;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.ProductDto;
import kz.cake.web.model.ProductFilterDto;
import kz.cake.web.repository.CityRepository;
import kz.cake.web.service.base.DictionaryService;

import java.util.List;
import java.util.Optional;

public class CityService extends DictionaryService<City, CityRepository> {
    private final ProductService productService;

    public CityService() {
        super();
        this.repository = new CityRepository();
        this.supplier = () -> new City();
        productService = new ProductService();
    }

    @Override
    public String cacheKey() {
        return "City";
    }

    @Override
    public String cacheKeyWithLocal() {
        return "CityWithLocal";
    }

    @Override
    public void delete(DictionaryDto dictionary) throws CustomValidationException {
        List<ProductDto> find = productService.find(new ProductFilterDto());
        if (!find.isEmpty()) {
            throw new CustomValidationException("error.activeRecord", ActionNames.CityList);
        }
        super.delete(dictionary);
    }

    @Override
    protected DictionaryDto mapWithLocal(City m) {
        DictionaryDto dictionaryDto = new DictionaryDto(m.getId(), m.getCode(), m.isActive());
        Optional<Local> local = localService.findByCode(m.getCode());
        if (local.isPresent()) {
            dictionaryDto.setText(local.get().getMessage());
        }
        return dictionaryDto;
    }

    @Override
    protected DictionaryDto map(City m) {
        return new DictionaryDto(m.getId(), m.getCode(), m.isActive());
    }
}
