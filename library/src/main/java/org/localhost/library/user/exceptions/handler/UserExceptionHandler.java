package org.localhost.library.user.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.localhost.library.book.exceptions.handler.BookExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class UserExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UserExceptionHandler.ValidationErrorResponse> handleValidationErrors(
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
