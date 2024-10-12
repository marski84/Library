package org.localhost.library.book.exceptions;

import org.localhost.library.book.exceptions.messages.BookExceptionMessages;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException() {
        super(BookExceptionMessages.BOOK_EXISTS);
    }
}
