package info.theinside.test.model;

import lombok.Data;

@Data
public class Error {
    private int statusCode;
    private String message;

    public Error(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}