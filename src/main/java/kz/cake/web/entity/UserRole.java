package kz.cake.web.entity;

public class UserRole{
    private Long userId;
    private Long roleId;

    public UserRole(Long userId, Long roleId){
        this.userId = userId;
        this.roleId = roleId;
    }
}
