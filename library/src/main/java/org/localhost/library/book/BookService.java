package org.localhost.library.book;

import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.model.Book;

public interface BookService {
    Book registerBook(BookRegistrationDto bookData);
}
