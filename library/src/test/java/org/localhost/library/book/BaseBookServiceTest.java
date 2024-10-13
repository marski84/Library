package org.localhost.library.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.localhost.library.book.dto.BookDto;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.dto.EditBookDto;
import org.localhost.library.book.exceptions.BookAlreadyExistsException;
import org.localhost.library.book.exceptions.BookNotFoundException;
import org.localhost.library.book.model.Book;
import org.localhost.library.repositories.InMemoryBookRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseBookServiceTest {

    private BaseBookService objectUnderTest;
    private BookRegistrationDto bookRegistrationDto;
    private final long NON_EXISTING_BOOK_ID = 100l;

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

    @Test
    @DisplayName("removeBook should successfully remove book")
    void removeBook() {
//        given
        Book book = objectUnderTest.registerBook(bookRegistrationDto);
//        when
        BookDto testResult = objectUnderTest.removeBook(book.getId());
//        then
        assertAll(
                ()-> assertEquals(book.getId(), testResult.getId()),
                ()-> assertEquals(book.getIsbn(), testResult.getIsbn()),
                ()-> assertEquals(book.getTitle(), testResult.getTitle())
        );
    }


    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    @DisplayName("removeBook should throw when id 0 or negative")
    void removeBookWithNegativeId(long id) {
        assertThrows(
                IllegalArgumentException.class,
                () -> objectUnderTest.removeBook(id)
        );
    }

    @Test
    @DisplayName("removeBook should throw when book not found")
    void removeBookWithBookNotFound() {
        assertThrows(
                BookNotFoundException.class,
                () -> objectUnderTest.removeBook(NON_EXISTING_BOOK_ID)
        );
    }


    @Test
    @DisplayName("getBookById should return book data")
    void getBookById() {
//        given
        Book book = objectUnderTest.registerBook(bookRegistrationDto);
//        when
        BookDto testResult = objectUnderTest.getBookById(book.getId());
//        then
        assertAll(
                ()-> assertEquals(book.getTitle(), testResult.getTitle()),
                ()-> assertEquals(book.getAuthor(), testResult.getAuthor()),
                ()-> assertEquals(book.getPublisher(), testResult.getPublisher()),
                ()-> assertEquals(book.getPages(), testResult.getPages()),
                ()-> assertEquals(book.getIsbn(), testResult.getIsbn())
        );
    }

    @Test
    @DisplayName("getBookById should throw when book not found")
    void getBookByIdWithBookNotFound() {
        assertThrows(
                BookNotFoundException.class,
                () -> objectUnderTest.getBookById(NON_EXISTING_BOOK_ID)
        );
    }

    @Test
    @DisplayName("getAllBooks should return a list of books")
    void getAllBooks() {
//        given
        Book book = objectUnderTest.registerBook(bookRegistrationDto);
        BookRegistrationDto secondBookRegDto = BookRegistrationDto.builder()
                .title("second")
                .author("some")
                .pages(1111)
                .publisher("pub")
                .isbn("sadas92-sdfsa")
                .build();

        Book secondBook = objectUnderTest.registerBook(secondBookRegDto);
//        when
        List<BookDto> testResult = objectUnderTest.getAllBooks();
//        then
        assertAll(
                ()-> assertEquals(2, testResult.size()),
                ()-> assertEquals(book.getId(), testResult.get(0).getId()),
                ()-> assertEquals(secondBook.getId(), testResult.get(1).getId())
        );
    }

    @Test
    @DisplayName("editBook should successfully update book data")
    void editBook() {
//        given
        Book book = objectUnderTest.registerBook(bookRegistrationDto);
        EditBookDto editBookDto = EditBookDto.builder()
                .author("new")
                .title("updated title")
                .pages(999)
                .publisher("updated publisher")
                .build();
//        when
        BookDto testResult = objectUnderTest.editBook(book.getId(), editBookDto);

//        then
        assertAll(
                () -> assertEquals(editBookDto.getAuthor(), testResult.getAuthor()),
                () -> assertEquals(editBookDto.getTitle(), testResult.getTitle()),
                () -> assertEquals(editBookDto.getPages(), testResult.getPages()),
                () -> assertEquals(editBookDto.getPublisher(), testResult.getPublisher()),
                () -> assertEquals(book.getId(), testResult.getId()),
                () -> assertEquals(book.getIsbn(), testResult.getIsbn())
        );
    }

    @Test
    @DisplayName("editBook should throwe when book is not found")
    void editBookWithBookNotFound() {
        EditBookDto editBookDto = EditBookDto.builder()
                .author("new")
                .title("updated title")
                .pages(999)
                .publisher("updated publisher")
                .build();

        assertThrows(
                BookNotFoundException.class,
                ()-> objectUnderTest.editBook(NON_EXISTING_BOOK_ID, editBookDto)
        );
    }

}