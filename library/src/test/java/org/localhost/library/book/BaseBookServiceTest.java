package org.localhost.library.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.exceptions.BookAlreadyExistsException;
import org.localhost.library.book.model.Book;
import org.localhost.library.repositories.InMemoryBookRepository;

import static org.junit.jupiter.api.Assertions.*;

class BaseBookServiceTest {

    private BaseBookService objectUnderTest;
    private BookRegistrationDto bookRegistrationDto;

    @BeforeEach
    void setUp() {
        BookRepository localBookRepository = new InMemoryBookRepository();
        objectUnderTest = new BaseBookService(localBookRepository);
        bookRegistrationDto = BookRegistrationDto.builder()
                .title("Test book")
                .author("Test Author")
                .pages(398)
                .publisher("some publisher")
                .isbn("12412341")
                .build();
    }

    @Test
    @DisplayName("registerBook should successfully register new book")
    void registerBook() {
//        given, when
        Book testResult = objectUnderTest.registerBook(bookRegistrationDto);
//        then
        assertAll(
                () -> assertNotNull(testResult),
                () -> assertEquals(bookRegistrationDto.getTitle(), testResult.getTitle()),
                () -> assertEquals(bookRegistrationDto.getPublisher(), testResult.getPublisher()),
                () -> assertEquals(bookRegistrationDto.getAuthor(), testResult.getAuthor()),
                () -> assertEquals(bookRegistrationDto.getIsbn(), testResult.getIsbn()),
                () -> assertEquals(bookRegistrationDto.getPages(), testResult.getPages())
        );
    }

    @Test
    @DisplayName("registerBook should throw when book is null")
    void registerBookWithNullBook() {
        assertThrows(
                IllegalArgumentException.class,
                () -> objectUnderTest.registerBook(null));
    }

    @Test
    @DisplayName("registerBook should throw trying to register book with duplicate isbn")
    void registerBookWithDuplicateIsbn() {
//        given
        objectUnderTest.registerBook(bookRegistrationDto);
//        when, then
        assertThrows(
                BookAlreadyExistsException.class,
                () -> objectUnderTest.registerBook(bookRegistrationDto)
        );
    }

}