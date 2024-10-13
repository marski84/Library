package org.localhost.library.book.exceptions;

import org.localhost.library.book.exceptions.messages.BookExceptionMessages;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
        super(BookExceptionMessages.BOOK_NOT_FOUND);
    }
}
