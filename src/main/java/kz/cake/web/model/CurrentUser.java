package kz.cake.web.model;

import java.util.HashSet;
import java.util.Set;

public class CurrentUser {
    private Long userId;
    private String userName;
    private Set<String> roles = new HashSet<>();

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
