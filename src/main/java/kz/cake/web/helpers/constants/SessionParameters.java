package kz.cake.web.helpers.constants;

public enum SessionParameters {
    language("language"),
    languages("languages"),
    languageId("languageId"),
    locals("locals"),
    errors("errors"),
    user("user"),
    users("users"),
    cities("cities"),
    productSizes("productSizes"),
    productCategories("productCategories"),
    products("products"),
    item("item"),
    orders("orders");

    String name;

    SessionParameters(String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return name;
    }
}
