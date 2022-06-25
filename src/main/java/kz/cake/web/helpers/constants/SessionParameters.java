package kz.cake.web.helpers.constants;

public enum SessionParameters {
    language("language"),
    languageId("languageId"),
    errors("errors"),
    user("user"),
    users("users"),
    dictionary("dictionary");

    String name;

    SessionParameters(String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return name;
    }
}
