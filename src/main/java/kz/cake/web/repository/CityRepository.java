package kz.cake.web.repository;

import kz.cake.web.entity.City;
import kz.cake.web.repository.base.DictionaryRepository;

public class CityRepository extends DictionaryRepository<City> {
    public CityRepository() {
        supplier = () -> new City();
    }
}
