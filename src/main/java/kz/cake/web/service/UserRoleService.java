package kz.cake.web.service;

import kz.cake.web.entity.UserRole;
import kz.cake.web.repository.UserRoleRepository;
import kz.cake.web.service.base.BaseService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.util.List;

public class UserRoleService extends BaseService<UserRole, UserRoleRepository> {
    private final Logger logger = LogManager.getLogger(UserService.class);

    public UserRoleService() {
        this.repository = new UserRoleRepository();
    }

    public List<UserRole> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
}
