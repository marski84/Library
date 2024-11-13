package org.localhost.library.book.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.localhost.library.book.exceptions.BookException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class BookExceptionHandler {

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
                .body(new ValidationErrorResponse(
                        "Validation failed",
                        errors,
                        ZonedDateTime.now()
                ));
    }

    @ExceptionHandler(BookException.class)
    public ResponseEntity<BookExceptionErrorResponse> handleBookException(BookException ex) {
        return ResponseEntity
                .badRequest()
                .body(
                        new BookExceptionErrorResponse(
                                ex.getMessage(),
                                ex.getErrorCode().getCode(),
                                ZonedDateTime.now()
                        )
                );
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
    private static class BookExceptionErrorResponse {
        private String message;
        private int errorCode;
        private ZonedDateTime timestamp;
    }
}
