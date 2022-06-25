package kz.cake.web.helpers.constants;

public enum SessionParameters {
    language("language"),
    languages("languages"),
    languageId("languageId"),
    locals("locals"),
    errors("errors"),
    user("user"),
    users("users"),
    cities("cities");

    String name;

    SessionParameters(String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return name;
    }
}
