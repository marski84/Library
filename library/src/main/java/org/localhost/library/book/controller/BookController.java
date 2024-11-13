package org.localhost.library.book.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.localhost.library.book.dto.BookDto;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.dto.EditBookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BookController {
    ResponseEntity<BookDto> registerNewBook(@Valid @RequestBody BookRegistrationDto book);

    ResponseEntity<List<BookDto>> getAllBooks();

    ResponseEntity<Long> removeBook(@PathVariable @Positive Long id);

    ResponseEntity<BookDto> getBookById(@PathVariable @Positive Long id);

    ResponseEntity<BookDto> editBook(@PathVariable @Positive Long id, @Valid @RequestBody EditBookDto bookDto);
}
