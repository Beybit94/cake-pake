package kz.cake.web.service;

import kz.cake.web.entity.City;
import kz.cake.web.repository.CityRepository;
import kz.cake.web.service.base.DictionaryService;

public class CityService extends DictionaryService<City, CityRepository> {
    public CityService() {
        this.repository = new CityRepository();
    }

    @Override
    public String cacheKey() {
        return "City";
    }

    @Override
    public String cacheKeyWithLocal() {
        return "CityWithLocal";
    }
}
