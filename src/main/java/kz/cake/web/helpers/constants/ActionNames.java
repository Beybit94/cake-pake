package kz.cake.web.helpers.constants;

import java.util.Arrays;
import java.util.List;

public enum ActionNames {
    LanguagesChange("LanguagesChange"),
    LanguagesList("LanguagesList"),
    AuthLogin("AuthLogin"),
    AuthRegister("AuthRegister"),
    AuthLogout("AuthLogout"),
    AuthProfile("AuthProfile"),
    AuthChange("AuthChange"),
    UserList("UserList"),
    UserAdd("UserAdd"),
    UserEdit("UserEdit"),
    UserRemove("UserRemove"),
    UserReset("UserReset"),
    UserUnblock("UserUnblock"),
    LocalList("LocalList"),
    LocalAdd("LocalAdd"),
    LocalEdit("LocalEdit"),
    LocalRemove("LocalRemove"),
    CityList("CityList"),
    CityAdd("CityAdd"),
    CityEdit("CityEdit"),
    CityRemove("CityRemove"),
    SizeList("SizeList"),
    SizeAdd("SizeAdd"),
    SizeEdit("SizeEdit"),
    SizeRemove("SizeRemove"),
    CategoryList("CategoryList"),
    CategoryAdd("CategoryAdd"),
    CategoryEdit("CategoryEdit"),
    CategoryRemove("CategoryRemove"),
    ProductList("ProductList"),
    ProductMy("ProductMy"),
    ProductAdd("ProductAdd"),
    ProductEdit("ProductEdit"),
    ProductRead("ProductRead"),
    ProductDetail("ProductDetail"),
    ProductRemove("ProductRemove"),
    PhotoRemove("PhotoRemove"),
    CommentAdd("CommentAdd"),
    OrderMy("OrderMy"),
    OrderCart("OrderCart"),
    OrderAdd("OrderAdd"),
    OrderHistory("OrderHistory");

    public static final List<ActionNames> userPermissions = Arrays.asList(LanguagesChange, AuthLogin, AuthRegister, AuthLogout, AuthProfile, AuthChange, OrderHistory, OrderCart, OrderAdd, ProductList, ProductDetail, CommentAdd);
    public static final List<ActionNames> managerPermissions = Arrays.asList(LanguagesChange, AuthLogin, AuthRegister, AuthLogout, AuthProfile, AuthChange, ProductMy, ProductAdd, ProductEdit, ProductRemove, ProductRead, PhotoRemove, ProductList, ProductDetail, OrderMy);

    String name;

    ActionNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
