package kz.cake.web.helpers.constants;

public enum PageNames {
    main("/main.jsp"),
    login("/login.jsp"),
    register("/register.jsp");

    String name;

    PageNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
