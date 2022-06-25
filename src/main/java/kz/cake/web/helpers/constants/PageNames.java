package kz.cake.web.helpers.constants;

public enum PageNames {
    main("main.jsp"),
    error("error.jsp"),
    access_denied("access_denied.jsp"),
    login("login.jsp"),
    register("register.jsp"),
    users("users.jsp"),
    languages("languages.jsp"),
    locals("locals.jsp"),
    roles("roles.jsp"),
    city("city.jsp"),
    product_size("product-size.jsp"),
    product_category("product-category.jsp");

    String name;

    PageNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
