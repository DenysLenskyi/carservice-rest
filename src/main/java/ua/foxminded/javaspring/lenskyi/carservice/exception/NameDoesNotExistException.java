package ua.foxminded.javaspring.lenskyi.carservice.exception;

public class NameDoesNotExistException extends RuntimeException {

    public NameDoesNotExistException(String message) {
        super(message);
    }
}