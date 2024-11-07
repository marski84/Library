package org.localhost.library.library.services.QueryService;

import org.localhost.library.book.dto.BookDto;
import org.localhost.library.library.RentalStatus;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.dto.RentalStatisticsDto;
import org.localhost.library.library.exceptions.RentalException;
import org.localhost.library.user.dto.UserDto;

import java.time.ZonedDateTime;
import java.util.List;

public interface RentalQueryService {
    List<RentalDto> getRentalDataForBook(long bookId);

    List<RentalDto> getRentalDataForUser(long userId);

    List<RentalDto> getActiveRentals();

    List<RentalDto> getOverdueRentals();

    RentalDto getCurrentRentalForBook(long bookId);

    boolean isBookAvailableForRental(long bookId);

    int getNumberOfActiveRentalsForUser(long userId);

    List<BookDto> getMostPopularBooks(int limit);

    List<UserDto> getMostActiveUsers(int limit);

    RentalStatisticsDto getRentalStatistics();

    void extendRental(long rentalId, int days) throws RentalException;

    RentalStatus checkRentalStatus(ZonedDateTime dueDate, ZonedDateTime returnDate);
}