package kz.cake.web.model;

import kz.cake.web.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private User user;
    private List<DictionaryDto> roles = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
    }

    public void setRoles(List<DictionaryDto> roles) {
        this.roles = roles;
    }

    public User getUser() {
        return user;
    }

    public List<DictionaryDto> getRoles() {
        return roles;
    }
}
