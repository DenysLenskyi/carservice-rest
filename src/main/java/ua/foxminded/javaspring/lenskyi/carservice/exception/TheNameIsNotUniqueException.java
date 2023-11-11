package ua.foxminded.javaspring.lenskyi.carservice.exception;

public class TheNameIsNotUniqueException extends RuntimeException {

    public TheNameIsNotUniqueException(String message) {
        super(message);
    }
}