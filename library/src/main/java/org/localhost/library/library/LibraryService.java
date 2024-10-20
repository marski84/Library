package org.localhost.library.library;

import org.localhost.library.book.dto.BookDto;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.dto.RentalStatisticsDto;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.exceptions.RentalExtensionNotAllowedException;
import org.localhost.library.library.model.Rental;
import org.localhost.library.user.dto.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LibraryService {

    SuccessfulRentalDto rentBookToUser(long bookId, long userId);
    Rental registerBookReturn(long bookId, long userId, Instant returnDate);
    void calculatePenaltyPoints(long userId, int maxPenaltyPoints, RentalStatus rentalStatus);
    List<RentalDto> getRentalDataForBook(long bookId);
    List<RentalDto> getRentalDataForUser(long userId);
    List<RentalDto> getActiveRentals();
    List<RentalDto> getOverdueRentals();
    Optional<RentalDto> getCurrentRentalForBook(long bookId);
    boolean isBookAvailableForRental(long bookId);
    int getNumberOfActiveRentalsForUser(long userId);
    List<BookDto> getMostPopularBooks(int limit);
    List<UserDto> getMostActiveUsers(int limit);
    RentalStatisticsDto getRentalStatistics();
    void extendRental(long rentalId, int days) throws RentalExtensionNotAllowedException;
}
