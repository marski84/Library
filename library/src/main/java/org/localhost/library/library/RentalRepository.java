package org.localhost.library.library;

import org.localhost.library.library.model.Rental;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RentalRepository extends CrudRepository<Rental, Long> {
    Optional<Rental> findByBookIdAndRentDateIsEmpty(long bookId);
    Optional<Rental> findByUserId(String userId);
    Optional<Rental> findRentalByBookIdAndUserId(long bookId, long userId);
}
