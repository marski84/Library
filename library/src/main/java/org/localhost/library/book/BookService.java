package org.localhost.library.book;

import org.localhost.library.book.dto.BookDto;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.dto.EditBookDto;
import org.localhost.library.book.model.Book;

import java.util.List;

public interface BookService {
    Book registerBook(BookRegistrationDto bookData);
    BookDto removeBook(long bookId);
    Book getBookById(long bookId);
    List<BookDto> getAllBooks();
    BookDto editBook(long bookId, EditBookDto bookData);
}
