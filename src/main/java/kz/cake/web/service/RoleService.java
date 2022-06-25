package kz.cake.web.service;

import kz.cake.web.entity.Role;
import kz.cake.web.repository.RoleRepository;
import kz.cake.web.service.base.DictionaryService;;


public class RoleService extends DictionaryService<Role, RoleRepository> {
    public RoleService() {
        this.repository = new RoleRepository();
    }

    @Override
    public String cacheKey() {
        return "Role";
    }

    @Override
    public String cacheKeyWithLocal() {
        return "RoleWithLocal";
    }
}
