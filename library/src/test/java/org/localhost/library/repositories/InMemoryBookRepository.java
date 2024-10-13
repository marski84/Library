package org.localhost.library.repositories;

import org.localhost.library.book.BookRepository;
import org.localhost.library.book.model.Book;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.StreamSupport;

public class InMemoryBookRepository implements BookRepository {
    Map<Long, Book> books = new HashMap<>();
    AtomicLong idGenerator = new AtomicLong();


    @Override
    public Boolean existsBookByIsbn(String isbn) {
        return books.values().stream().anyMatch(book -> book.getIsbn().equals(isbn));
    }

    @Override
    public Book save(Book entity) {
        if (entity.getId() == 0) {
            long id = idGenerator.incrementAndGet();
            entity.setId(id);
            books.put(id, entity);
            return books.get(id);
        } else {
            books.replace(entity.getId(), entity);
            return books.get(entity.getId());
        }
    }

    @Override
    public <S extends Book> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Book> findById(Long aLong) {
        return books.values().stream().filter(book -> book.getId() == aLong).findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Book> findAll() {
        return books.values();
    }

    @Override
    public Iterable<Book> findAllById(Iterable<Long> longs) {
        return null;
    }


    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }



    @Override
    public void delete(Book entity) {
        books.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Book> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
