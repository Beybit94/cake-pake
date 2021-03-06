package kz.cake.web.helpers.constants;

public enum PageNames {
    main("main.jsp"),
    error("error.jsp"),
    access_denied("access_denied.jsp"),
    login("login.jsp"),
    register("register.jsp"),
    profile("profile.jsp"),
    users("users.jsp"),
    languages("languages.jsp"),
    locals("locals.jsp"),
    roles("roles.jsp"),
    city("city.jsp"),
    product_size("product-size.jsp"),
    product_category("product-category.jsp"),
    my_product("my-product.jsp"),
    edit_product("edit-product.jsp"),
    detail_product("detail-product.jsp"),
    orders("orders.jsp"),
    history("history.jsp"),
    shopping_cart("shopping-cart.jsp");

    String name;

    PageNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
