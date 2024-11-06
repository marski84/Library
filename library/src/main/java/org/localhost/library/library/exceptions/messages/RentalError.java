package org.localhost.library.library.exceptions.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalError {
    NOT_VALID_RETURN_DATE(1000, "Not valid return date"),
    RENTAL_EXTENSION_NOT_ALLOWED(1001, "Rental extension not allowed"),
    RENTAL_NOT_FOUND(1002, "Rental not found"),
    RENTAL_NOT_POSSIBLE(1003, "Rental not possible");

    private final int code;
    private final String message;
}
