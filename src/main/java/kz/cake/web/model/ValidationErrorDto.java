package kz.cake.web.model;

public class ValidationErrorDto {
    private String text;

    public ValidationErrorDto(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
