package kz.cake.web.exceptions;

import kz.cake.web.helpers.constants.ActionNames;

public class CustomValidationException extends Exception {
    private String messageCode;
    private ActionNames redirect;

    public CustomValidationException(String messageCode, ActionNames redirect) {
        super();
        this.messageCode = messageCode;
        this.redirect = redirect;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public ActionNames getRedirect() {
        return redirect;
    }
}
