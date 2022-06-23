package kz.cake.web.helpers.constants;

public enum ActionNames {
    UserLogin("UserLogin"),
    UserRegister("UserRegister"),
    UserLogout("UserLogout"),
    UserProfile("UserProfile"),
    UserList("UserList"),
    UserAdd("UserAdd"),
    UserEdit("UserEdit"),
    UserRemove("UserRemove"),
    UserReset("UserReset"),
    UserUnblock("UserUnblock"),
    LanguagesChange("LanguagesChange");

    String name;

    ActionNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
