package kz.cake.web.repository;

import kz.cake.web.entity.City;
import kz.cake.web.entity.Product;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.repository.base.DictionaryRepository;

import java.util.Optional;

public class CityRepository extends DictionaryRepository<City> {
    private final ProductRepository productRepository;

    public CityRepository() {
        supplier = () -> new City();
        productRepository = new ProductRepository();
    }

    @Override
    public void delete(City entity) throws CustomValidationException {
        Optional<Product> find = productRepository.getAll().stream().filter(m -> m.getCityId().equals(entity.getId())).findAny();
        if (find.isPresent()) {
            throw new CustomValidationException("error.activeRecord", ActionNames.CityList);
        }

        super.delete(entity);
    }
}
