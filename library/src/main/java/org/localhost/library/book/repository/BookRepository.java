package org.localhost.library.book.repository;

import org.localhost.library.book.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
   Boolean existsBookByIsbn(String isbn);
}
