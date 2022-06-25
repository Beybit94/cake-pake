package kz.cake.web.repository;

import kz.cake.web.entity.City;
import kz.cake.web.repository.base.DictionaryRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CityRepository extends DictionaryRepository<City> {
    private final Logger logger = LogManager.getLogger(CityRepository.class);

    public CityRepository() {
        supplier = () -> new City();
    }
}
