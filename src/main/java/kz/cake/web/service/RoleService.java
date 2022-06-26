package kz.cake.web.service;

import kz.cake.web.entity.Local;
import kz.cake.web.entity.Role;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.repository.RoleRepository;
import kz.cake.web.service.base.DictionaryService;

import java.util.Optional;

public class RoleService extends DictionaryService<Role, RoleRepository> {
    public RoleService() {
        super();
        this.repository = new RoleRepository();
        this.supplier = () -> new Role();
    }

    @Override
    public String cacheKey() {
        return "Role";
    }

    @Override
    public String cacheKeyWithLocal() {
        return "RoleWithLocal";
    }

    @Override
    protected DictionaryDto mapWithLocal(Role m) {
        DictionaryDto dictionaryDto = new DictionaryDto(m.getId(), m.getCode(), m.isActive());
        Optional<Local> local = localService.getByCode(m.getCode());
        if (local.isPresent()) {
            dictionaryDto.setText(local.get().getMessage());
        }
        return dictionaryDto;
    }

    @Override
    protected DictionaryDto map(Role m) {
        return new DictionaryDto(m.getId(), m.getCode(), m.isActive());
    }
}
