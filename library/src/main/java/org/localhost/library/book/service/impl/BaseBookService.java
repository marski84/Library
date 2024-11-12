package org.localhost.library.book.service.impl;

import org.localhost.library.book.dto.BookDto;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.dto.EditBookDto;
import org.localhost.library.book.exceptions.BookException;
import org.localhost.library.book.exceptions.messages.BookError;
import org.localhost.library.book.model.Book;
import org.localhost.library.book.repository.BookRepository;
import org.localhost.library.book.service.BookService;
import org.localhost.library.utils.AppLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class BaseBookService implements BookService {

    private final BookRepository bookRepository;


    public BaseBookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Book registerBook(BookRegistrationDto bookData) {
        if (bookData == null) {
            AppLogger.logError("Book registration data cannot be null");
            throw new IllegalArgumentException("Book registration data cannot be null");
        }

//        ISBN is unique- we have to validate it
        if (bookRepository.existsBookByIsbn(bookData.getIsbn())) {
            BookException bookExistsException = new BookException(BookError.BOOK_ALREADY_EXISTS);
            AppLogger.logError(bookExistsException.getErrorCode() + " book isbn: " + bookData.getIsbn());
            throw bookExistsException;
        }
        Book book = new Book();
        book.setTitle(bookData.getTitle());
        book.setAuthor(bookData.getAuthor());
        book.setPublisher(bookData.getPublisher());
        book.setIsbn(bookData.getIsbn());
        book.setPages(bookData.getPages());

        AppLogger.logInfo("Book registration successful for " + book);
        return bookRepository.save(book);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookDto removeBook(long bookId) {
        validateBookId(bookId);
        Book bookToRemove = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    BookException bookNotFoundException = new BookException(BookError.BOOK_NOT_FOUND);
                    AppLogger.logError(bookNotFoundException.getErrorCode() + "for bookId: " + bookId);
                    return bookNotFoundException;
                });


        bookRepository.delete(bookToRemove);
        AppLogger.logInfo("Book with ID " + bookId + " removed successfully");
        return BookDto.builder()
                .id(bookToRemove.getId())
                .isbn(bookToRemove.getIsbn())
                .title(bookToRemove.getTitle())
                .build();
    }

    @Override
    public Book getBookById(long bookId) {
        validateBookId(bookId);

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> {
                    BookException bookNotFoundException = new BookException(BookError.BOOK_NOT_FOUND);
                    AppLogger.logError(bookNotFoundException.getErrorCode() + " for bookId: " + bookId);
                    return bookNotFoundException;
                });
        AppLogger.logInfo("Book with ID " + bookId + " found");
        return book;
    }

    @Override
    public List<BookDto> getAllBooks() {
        Iterable<Book> books = bookRepository.findAll();
        AppLogger.logInfo("Books found");
        return StreamSupport.stream(books.spliterator(), false)
                .map(book -> BookDto.builder()
                        .id(book.getId())
                        .isbn(book.getIsbn())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .build()).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookDto editBook(long bookId, EditBookDto bookData) {
        validateBookId(bookId);

        if (bookData == null) {
            AppLogger.logError("edit book data cannot be null for id: " + bookId);
            throw new IllegalArgumentException("Book edit data cannot be null");
        }

        Book bookToEdit = bookRepository.findById(bookId).orElseThrow(() -> new BookException(BookError.BOOK_NOT_FOUND));

        bookToEdit.setTitle(bookData.getTitle());
        bookToEdit.setAuthor(bookData.getAuthor());
        bookToEdit.setPublisher(bookData.getPublisher());
        bookToEdit.setPages(bookData.getPages());

        Book updateBook = bookRepository.save(bookToEdit);
        AppLogger.logInfo("Book with ID " + bookId + " edited successfully");

        AppLogger.logDebug("creating book dto " + bookData);
        return BookDto.builder()
                .id(updateBook.getId())
                .isbn(updateBook.getIsbn())
                .author(updateBook.getAuthor())
                .title(updateBook.getTitle())
                .pages(updateBook.getPages())
                .publisher(updateBook.getPublisher())
                .build();
    }


    private void validateBookId(long bookId) {
        if (bookId <= 0) {
            AppLogger.logError("book id cannot be null or negative");
            throw new IllegalArgumentException("Book id cannot be less than zero");
        }
    }
}
