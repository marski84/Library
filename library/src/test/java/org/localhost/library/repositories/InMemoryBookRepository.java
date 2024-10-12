package org.localhost.library.repositories;

import org.localhost.library.book.BookRepository;
import org.localhost.library.book.model.Book;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

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
    public Optional<Book> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<Book> findAll() {
        return null;
    }

    @Override
    public Iterable<Book> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Book entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Book> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
