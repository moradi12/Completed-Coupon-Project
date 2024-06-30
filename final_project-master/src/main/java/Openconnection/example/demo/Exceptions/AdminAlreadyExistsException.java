package Openconnection.example.demo.Exceptions;

public class AdminAlreadyExistsException extends Exception {
    public AdminAlreadyExistsException() {
        super("Admin already exists.");
    }

    public AdminAlreadyExistsException(String message) {
        super(message);
    }
}
