package kz.cake.web.service;

import kz.cake.web.entity.Local;
import kz.cake.web.entity.Role;
import kz.cake.web.model.RoleDto;
import kz.cake.web.repository.RoleRepository;;

import java.util.Optional;

public class RoleService extends BaseService<Role, RoleRepository> {
    private final LocalService localService;
    public RoleService() {
        this.repository = new RoleRepository();
        localService = new LocalService();
    }

    public Optional<Role> findByCode(String code) {
        return repository.findByCode(code);
    }

    public RoleDto getById(Long id) {
        Role role = repository.getById(id);
        Local local = localService.getByCode(role.getCode());

        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setCode(role.getCode());
        roleDto.setText(local.getMessage());
        roleDto.setActive(role.isActive());

        return roleDto;
    }
}
