package kz.cake.web.helpers.constants;

public enum RequestParameters {
    id("id"),
    role("role"),
    sex("sex"),
    city("city"),
    code("code"),
    text("text"),
    price("price"),
    parent("parent"),
    toPrice("toPrice"),
    payment("payment"),
    comment("comment"),
    confirm("confirm"),
    address("address"),
    redirect("redirect"),
    username("username"),
    password("password"),
    fromPrice("fromPrice"),
    languageId("languageId"),
    productName("name"),
    description("description"),
    productSize("productSize"),
    productCategory("productCategory");

    String name;

    RequestParameters(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
