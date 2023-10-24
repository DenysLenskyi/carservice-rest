package ua.foxminded.javaspring.lenskyi.carservice.exception;

public class SortingFieldDoesNotExistException extends RuntimeException {

    public SortingFieldDoesNotExistException(String message) {
        super(message);
    }
}