package kz.cake.web.helpers;

import kz.cake.web.model.CurrentUserDto;
import kz.cake.web.model.ValidationErrorDto;

import java.util.List;

public class CurrentSession {
    public static final CurrentSession Instance = new CurrentSession();
    private CurrentUserDto currentUser;
    private Long currentLanguageId;
    private String currentLanguageCode;
    private List<ValidationErrorDto> errors;

    public CurrentUserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUserDto currentUser) {
        this.currentUser = currentUser;
    }

    public Long getCurrentLanguageId() {
        return currentLanguageId;
    }

    public void setCurrentLanguageId(Long id) {
        currentLanguageId = id;
    }

    public String getCurrentLanguageCode() {
        return currentLanguageCode;
    }

    public void setCurrentLanguageCode(String currentLanguageCode) {
        this.currentLanguageCode = currentLanguageCode;
    }

    public List<ValidationErrorDto> getErrors() {
        List<ValidationErrorDto> _errors = errors;
        errors = null;
        return _errors;
    }

    public void setErrors(List<ValidationErrorDto> errors) {
        this.errors = errors;
    }

    public void clear() {
        currentUser = null;
        currentLanguageId = null;
        errors = null;
    }
}
