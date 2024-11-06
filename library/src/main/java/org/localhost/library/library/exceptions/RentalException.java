package org.localhost.library.library.exceptions;

import lombok.Getter;
import org.localhost.library.library.exceptions.messages.RentalError;

@Getter
public class RentalException extends RuntimeException {
    private final RentalError errorCode;

    public RentalException(RentalError errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
