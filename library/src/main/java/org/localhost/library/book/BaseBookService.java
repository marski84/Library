package org.localhost.library.book;

import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.exceptions.BookAlreadyExistsException;
import org.localhost.library.book.model.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new IllegalArgumentException("Book registration data cannot be null");
        }

//        ISBN is unique- we have to validate it
        if (bookRepository.existsBookByIsbn(bookData.getIsbn())) {
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
}
