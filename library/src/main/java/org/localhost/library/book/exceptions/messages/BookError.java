package org.localhost.library.book.exceptions.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookError {
    BOOK_NOT_FOUND(300, "Book with given ID does not exist"),
    BOOK_ALREADY_EXISTS(500, "Book with given ISBN already exists");

    private final int code;
    private final String message;

}
