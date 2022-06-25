package kz.cake.web.repository;

import kz.cake.web.entity.Role;
import kz.cake.web.repository.base.DictionaryRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RoleRepository extends DictionaryRepository<Role> {
    private final Logger logger = LogManager.getLogger(RoleRepository.class);

    public RoleRepository() {
        supplier = () -> new Role();
    }
}
