package kz.cake.web.model;

public class LocalDto {
    private Long id;
    private String code;
    private String text;
    private String language;

    public LocalDto(){

    }

    public LocalDto(Long id, String code, String text, String language){
        this.id = id;
        this.code = code;
        this.text = text;
        this.language = language;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
