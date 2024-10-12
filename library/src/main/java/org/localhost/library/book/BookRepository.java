package org.localhost.library.book;

import org.localhost.library.book.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {
   Boolean existsBookByIsbn(String isbn);
}
