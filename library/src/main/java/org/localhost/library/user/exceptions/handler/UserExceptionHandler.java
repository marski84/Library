package org.localhost.library.user.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.localhost.library.user.exceptions.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity
                .badRequest()
                .body(new UserExceptionHandler.ValidationErrorResponse(
                        "Validation failed",
                        errors,
                        ZonedDateTime.now()
                ));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<UserExceptionErrorResponse> handleUserException(UserException ex) {
        return ResponseEntity
                .badRequest()
                .body(new UserExceptionErrorResponse(
                        ex.getMessage(),
                        ex.getError().getCode(),
                        ZonedDateTime.now()
                ));
    }

    @Getter
    @AllArgsConstructor
    private static class ValidationErrorResponse {
        private String message;
        private List<String> errors;
        private ZonedDateTime timestamp;
    }

    @Getter
    @AllArgsConstructor
    private static class UserExceptionErrorResponse {
        private String message;
        private int errorCode;
        private ZonedDateTime timestamp;
    }


}
