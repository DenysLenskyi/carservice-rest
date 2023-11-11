package ua.foxminded.javaspring.lenskyi.carservice.exception;

public class IdDoesNotExistException extends RuntimeException {

    public IdDoesNotExistException(String message) {
        super(message);
    }
}