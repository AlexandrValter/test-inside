package info.theinside.test.exception;

public class BearerTokenIsNotFound extends RuntimeException {

    public BearerTokenIsNotFound(String message) {
        super(message);
    }
}
