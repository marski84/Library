package org.localhost.library.library;

import org.localhost.library.library.model.Rental;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends CrudRepository<Rental, Long> {
    Optional<Rental> findByBookIdAndRentDateIsEmpty(long bookId);
    Optional<Rental> findByUserId(long userId);
    Optional<Rental> findByBookId(long bookId);
    Optional<List<Rental>> findAllByBookId(long bookId);
    Optional<List<Rental>> findAllByUserId(long userId);
    List<Rental> findAllByReturnDateIsNull();

    @Query("SELECT r FROM Rental r WHERE r.returnDate IS NULL AND r.dueDate < :now ORDER BY r.dueDate DESC")
    List<Rental> findOverdueRentals(@Param("now") Instant now);

    Optional<Rental> findRentalByBookIdAndUserId(long bookId, long userId);
}
