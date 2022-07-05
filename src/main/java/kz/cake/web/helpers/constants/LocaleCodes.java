package kz.cake.web.helpers.constants;

public enum LocaleCodes {
    languageEn("en"),
    languageRu("ru"),
    roleAdmin("admin"),
    roleManager("manager"),
    roleUser("user"),
    userAdmin("admin"),
    userKulikov("kulikov"),
    userQulpinay("qulpinay"),
    userBeka("beka"),
    cityAlmaty("Almaty"),
    cityAstana("Astana"),
    statusDraft("draft"),
    statusNew("new"),
    statusCompleted("completed"),
    sizeLarge("large"),
    sizeMedium("medium"),
    sizeSmall("small"),
    categoryCake("cake"),
    categoryCheesecake("cheesecake"),
    categoryPie("pie"),
    categoryBiscuit("biscuit"),
    categoryBento("bento"),
    categoryTrifle("trifle"),
    categoryWhoopee("whoopee"),
    categorySpanish("spanish"),
    categoryClassic("classic"),
    categoryChocolate("chocolate"),
    categoryMeat("meat"),
    categoryOther("other");

    String name;

    LocaleCodes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
