package org.localhost.library.book.exceptions;

import lombok.Getter;
import org.localhost.library.book.exceptions.messages.BookError;

@Getter
public class BookException extends RuntimeException {
    private final BookError errorCode;

    public BookException(BookError errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
