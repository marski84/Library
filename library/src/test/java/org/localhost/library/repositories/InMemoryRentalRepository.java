package org.localhost.library.repositories;

import org.localhost.library.library.RentalRepository;
import org.localhost.library.library.model.Rental;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryRentalRepository implements RentalRepository {
    Map<Long, Rental> rentals = new HashMap<>();
    AtomicLong idGenerator = new AtomicLong();


    @Override
    public Optional<Rental> findByBookIdAndRentDateIsEmpty(long bookId) {
        return rentals.values().stream()
                .filter(r -> r.getBook().getId() == bookId)
                .findFirst();
    }

    @Override
    public Optional<Rental> findByUserId(long userId) {
        return null;
    }

    @Override
    public Optional<Rental> findByBookId(long bookId) {
        return Optional.empty();
    }

    @Override
    public List<Rental> findAllByBookId(long bookId) {
        return rentals.values().stream()
                .filter(rental -> rental.getBook().getId() == bookId)
                .toList();
    }

    @Override
    public List<Rental> findAllByUserId(long userId) {
        return rentals.values().stream()
                .filter(rental -> rental.getUser().getId() == userId)
                .toList();
    }

    @Override
    public List<Rental> findAllByReturnDateIsNull() {
        return rentals.values().stream()
                .filter(rental -> rental.getReturnDate() == null)
                .toList();
    }

    @Override
    public List<Rental> findOverdueRentals(ZonedDateTime now) {
        return List.of();
    }

    @Override
    public Optional<Rental> findRentalByBookIdAndUserId(long bookId, long userId) {
        return rentals.values().stream()
                .filter(rental -> rental.getUser().getId() == userId && rental.getBook().getId() == bookId)
                .findFirst();
    }

    @Override
    public Rental save(Rental entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.incrementAndGet());
            rentals.put(entity.getId(), entity);
        } else {
            rentals.put(entity.getId(), entity);
        }
        return rentals.get(entity.getId());
    }

    @Override
    public <S extends Rental> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Rental> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Rental> findAll() {
        return null;
    }

    @Override
    public Iterable<Rental> findAllById(Iterable<Long> longs) {
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
    public void delete(Rental entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Rental> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
