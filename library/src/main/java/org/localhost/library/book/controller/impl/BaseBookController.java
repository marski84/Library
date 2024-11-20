package org.localhost.library.book.controller.impl;

import org.localhost.library.book.controller.BookController;
import org.localhost.library.book.dto.BookDto;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.dto.EditBookDto;
import org.localhost.library.book.model.Book;
import org.localhost.library.book.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/books")
public class BaseBookController implements BookController {
    private final BookService bookService;

    public BaseBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/register")
    public ResponseEntity<BookDto> registerNewBook(@Validated @RequestBody BookRegistrationDto book) {
        Book newBook = bookService.registerBook(book);
        BookDto result = BookDto.fromBook(newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/get-books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> removeBook(@Validated @PathVariable Long id) {
        BookDto removedBook = bookService.removeBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(removedBook.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@Validated @PathVariable Long id) {
        Book book = bookService.getBookById(id);

        BookDto result = BookDto.fromBook(book);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> editBook(@PathVariable @Validated Long id, @RequestBody @Validated EditBookDto bookDto) {
        BookDto editedBook = bookService.editBook(id, bookDto);
        return ResponseEntity.status(HttpStatus.OK).body(editedBook);
    }
}
