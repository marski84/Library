package org.localhost.library.library.repository;

import org.localhost.library.library.model.Rental;
import org.localhost.library.user.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends CrudRepository<Rental, Long> {
    Optional<Rental> findByBookIdAndRentDateIsNull(long bookId);

    Optional<Rental> findByBookIdAndRentDateIsNotNull(long bookId);

    List<Rental> findAllByBookId(long bookId);

    List<Rental> findAllByUserId(long userId);
    List<Rental> findAllByReturnDateIsNull();

    @Query("SELECT r.book, COUNT(r) as rentCount FROM Rental r " +
            "GROUP BY r.book.id " +
            "ORDER BY r.rentDate DESC " +
            "LIMIT :limit")
    List<Rental> findMostPopularBooks(@Param("limit") int limit);

    @Query("SELECT r.user as user, COUNT(r) as rentalCount " +
            "FROM Rental r " +
            "GROUP BY r.user " +
            "ORDER BY rentalCount DESC " +
            "LIMIT :limit")
    List<User> findMostActiveUsers(@Param("limit") int limit);

//    @Query("SELECT r FROM Rental r WHERE r.returnDate IS NULL AND r.dueDate < :date ORDER BY r.dueDate DESC")
//    List<Rental> findOverdueRentals(@Param("now") ZonedDateTime date);

    @Query("SELECT r FROM Rental r WHERE r.returnDate IS NULL AND r.dueDate < :now ORDER BY r.dueDate DESC")
    List<Rental> findOverdueRentals(@Param("now") ZonedDateTime now);


    Optional<Rental> findRentalByBookIdAndUserId(long bookId, long userId);
}
