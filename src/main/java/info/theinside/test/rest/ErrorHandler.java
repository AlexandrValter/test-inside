package info.theinside.test.rest;

import info.theinside.test.exception.BearerTokenIsNotFound;
import info.theinside.test.exception.FailedAuthenticationException;
import info.theinside.test.exception.UsernameAlreadyExistException;
import info.theinside.test.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Вылавливает всплываемые exception и отправляет json в ответе с кодом ошибки и текстом сообщения об ошибке
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Error> catchDuplicateUsername(UsernameAlreadyExistException e) {
        return new ResponseEntity<>(new Error(HttpStatus.CONFLICT.value(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<Error> catchFailedAuthentication(FailedAuthenticationException e) {
        return new ResponseEntity<>(new Error(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<Error> catchFailedAuthentication(BearerTokenIsNotFound e) {
        return new ResponseEntity<>(new Error(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}