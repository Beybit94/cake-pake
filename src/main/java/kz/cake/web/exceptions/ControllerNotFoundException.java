package kz.cake.web.exceptions;

public class ControllerNotFoundException extends Exception {
    public ControllerNotFoundException(String message) {
        super(message);
    }
}
