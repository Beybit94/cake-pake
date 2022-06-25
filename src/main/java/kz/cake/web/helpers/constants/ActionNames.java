package kz.cake.web.helpers.constants;

import java.util.Arrays;
import java.util.List;

public enum ActionNames {
    LanguagesChange("LanguagesChange"),
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
    CityList("CityList"),
    CityAdd("CityAdd"),
    CityEdit("CityEdit"),
    CityRemove("CityRemove"),
    LocalList("LocalList"),
    LocalAdd("LocalAdd"),
    LocalEdit("LocalEdit"),
    LocalRemove("LocalRemove"),
    LanguagesList("LanguagesList");

    public static final List<ActionNames> userPermissions = Arrays.asList(LanguagesChange, UserLogin, UserRegister, UserLogout, UserProfile);
    public static final List<ActionNames> managerPermissions = Arrays.asList(LanguagesChange, UserLogin, UserRegister, UserLogout, UserProfile);

    String name;

    ActionNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
