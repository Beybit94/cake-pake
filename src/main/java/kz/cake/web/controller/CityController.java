package kz.cake.web.controller;

import kz.cake.web.controller.base.DictionaryController;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.service.CityService;

import java.util.HashMap;
import java.util.Map;

public class CityController extends DictionaryController<CityService> {
    public CityController() {
        this.service = new CityService();
    }

    @Override
    public Map<String, SessionParameters> getSessionParameters() {
        Map<String, SessionParameters> sessionParameters = new HashMap<>();
        sessionParameters.put("list", SessionParameters.cities);
        return sessionParameters;
    }

    @Override
    public Map<String, PageNames> getPageNames() {
        Map<String, PageNames> pageNames = new HashMap<>();
        pageNames.put("list", PageNames.city);
        return pageNames;
    }
}
