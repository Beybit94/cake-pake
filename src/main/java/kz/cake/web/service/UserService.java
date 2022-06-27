package kz.cake.web.service;

import kz.cake.web.entity.User;
import kz.cake.web.entity.UserRole;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.UserDto;
import kz.cake.web.repository.UserRepository;
import kz.cake.web.service.base.BaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService extends BaseService<User, UserRepository> {
    private final UserRoleService userRoleService;
    private final RoleService roleService;

    public UserService() {
        this.repository = new UserRepository();
        userRoleService = new UserRoleService();
        roleService = new RoleService();
    }

    public Optional<User> findUserByName(String userName) {
        return repository.findUserByName(userName);
    }

    public List<UserDto> getAllWithRole() {
        List<UserDto> userList = new ArrayList<>();
        for (User user : getAll()) {
            if(user.getUsername().equals(CurrentSession.Instance.getCurrentUser().getUserName())) continue;

            UserDto userDto = new UserDto();
            userDto.setUser(user);

            List<DictionaryDto> roles = new ArrayList<>();
            for (UserRole userRole : userRoleService.findByUserId(user.getId())) {
                roles.add(roleService.getByIdWithLocal(userRole.getRoleId()));
            }
            userDto.setRoles(roles);
            userList.add(userDto);
        }

        return userList;
    }

    public void setRole(String userName, String roleCode){
        findUserByName(userName).ifPresent(u->{
            roleService.findByCode(roleCode).ifPresent(r->{
                userRoleService.save(new UserRole(u.getId(),r.getId()));
            });
        });
    }

    public void delete(User user) throws CustomValidationException {
        repository.delete(user);
    }
}
