package Openconnection.example.demo.Exceptions;

public class LoginException extends Exception {
    public LoginException() {
        super("Login failed. Invalid credentials.");
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException(Throwable cause) {
        super("Login failed. Invalid credentials.", cause);
    }
}
