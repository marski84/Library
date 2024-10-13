package org.localhost.library.book;

import org.localhost.library.book.dto.BookDto;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.dto.EditBookDto;
import org.localhost.library.book.exceptions.BookAlreadyExistsException;
import org.localhost.library.book.exceptions.BookNotFoundException;
import org.localhost.library.book.model.Book;
import org.localhost.library.book.utils.AppLogger;
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
    @Transactional
    public Book registerBook(BookRegistrationDto bookData) {
        if (bookData == null) {
            AppLogger.logError("Book registration data cannot be null");
            throw new IllegalArgumentException("Book registration data cannot be null");
        }

//        ISBN is unique- we have to validate it
        if (bookRepository.existsBookByIsbn(bookData.getIsbn())) {
            AppLogger.logError("Book already exists " + bookData.getIsbn());
            throw new BookAlreadyExistsException();
        }
        Book book = new Book();
        book.setTitle(bookData.getTitle());
        book.setAuthor(bookData.getAuthor());
        book.setPublisher(bookData.getPublisher());
        book.setIsbn(bookData.getIsbn());
        book.setPages(bookData.getPages());

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public BookDto removeBook(long bookId) {
        validateBookId(bookId);
        Book bookToRemove = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    AppLogger.logError("Book with ID " + bookId + " not found");
                    return new BookNotFoundException();
                });


        bookRepository.delete(bookToRemove);
        return BookDto.builder()
                .id(bookToRemove.getId())
                .isbn(bookToRemove.getIsbn())
                .title(bookToRemove.getTitle())
                .build();
    }

    @Override
    public BookDto getBookById(long bookId) {
        validateBookId(bookId);

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> {
                    AppLogger.logError("Book with ID " + bookId + " not found");
                    return new BookNotFoundException();
                });
        return BookDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .pages(book.getPages())
                .isbn(book.getIsbn())
                .build();
    }

    @Override
    public List<BookDto> getAllBooks() {
        Iterable<Book> books = bookRepository.findAll();
        return StreamSupport.stream(books.spliterator(), false)
                .map(book -> BookDto.builder()
                        .id(book.getId())
                        .isbn(book.getIsbn())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .build()).toList();
    }

    @Override
    @Transactional
    public BookDto editBook(long bookId, EditBookDto bookData) {
        validateBookId(bookId);

        if (bookData == null) {
            AppLogger.logError("edit book data cannot be null for id: " + bookId);
            throw new IllegalArgumentException("Book edit data cannot be null");
        }

        Book bookToEdit = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);

        bookToEdit.setTitle(bookData.getTitle());
        bookToEdit.setAuthor(bookData.getAuthor());
        bookToEdit.setPublisher(bookData.getPublisher());
        bookToEdit.setPages(bookData.getPages());

        Book updateBook = bookRepository.save(bookToEdit);

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
