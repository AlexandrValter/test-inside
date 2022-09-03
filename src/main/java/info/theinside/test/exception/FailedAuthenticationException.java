package info.theinside.test.exception;

public class FailedAuthenticationException extends RuntimeException {

    public FailedAuthenticationException(String message) {
        super(message);
    }
}