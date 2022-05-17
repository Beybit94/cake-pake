package kz.cake.web.entity;

abstract class BaseDictionary<T> extends Base<T> {
    private String code;

    protected BaseDictionary(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
