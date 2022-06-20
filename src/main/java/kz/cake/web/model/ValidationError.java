package kz.cake.web.model;

public class ValidationError {
    private String text;

    public ValidationError(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
