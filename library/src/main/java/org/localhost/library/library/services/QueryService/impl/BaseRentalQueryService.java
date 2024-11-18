package org.localhost.library.library.services.QueryService.impl;

import org.localhost.library.book.dto.BookDto;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.dto.RentalStatisticsDto;
import org.localhost.library.library.exceptions.RentalException;
import org.localhost.library.library.exceptions.messages.RentalError;
import org.localhost.library.library.model.Rental;
import org.localhost.library.library.repository.RentalRepository;
import org.localhost.library.library.services.QueryService.RentalQueryService;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.utils.AppLogger;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class BaseRentalQueryService implements RentalQueryService {
    private final RentalRepository rentalRepository;


    public BaseRentalQueryService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<RentalDto> getRentalDataForBook(long bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book id must be greater than zero");
        }
        List<Rental> rentalList = rentalRepository.findAllByBookId(bookId);
        return rentalList.stream()
                .map(RentalDto::fromRental)
                .toList();
    }

    public List<RentalDto> getRentalDataForUser(long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User id must be greater than zero");
        }
        List<Rental> rentalList = rentalRepository.findAllByUserId(userId);
        return rentalList.stream()
                .map(RentalDto::fromRental)
                .toList();
    }

    public List<RentalDto> getActiveRentals() {
        List<Rental> activeRentals = rentalRepository.findAllByReturnDateIsNull();
        if (activeRentals.isEmpty()) {
            return List.of();
        }
        return activeRentals.stream()
                .map(RentalDto::fromRental)
                .toList();
    }

    public List<RentalDto> getOverdueRentals() {
        return rentalRepository.findOverdueRentals(ZonedDateTime.now()).stream()
                .map(RentalDto::fromRental)
                .toList();
    }

    public List<RentalDto> getOverdueRentals(ZonedDateTime date) {
        return rentalRepository.findOverdueRentals(date).stream()
                .map(RentalDto::fromRental)
                .toList();
    }

    public RentalDto getCurrentRentalForBook(long bookId) {
        return rentalRepository.findAllByBookId(bookId).stream()
                .filter(rental -> rental.getReturnDate() == null)
                .map(RentalDto::fromRental)
                .findFirst()
                .orElseThrow(() -> {
                            RentalException rentalException = new RentalException(RentalError.RENTAL_NOT_FOUND);
                            AppLogger.logError(rentalException.getErrorCode() + ": " + bookId);
                            return rentalException;
                        }
                );
    }

    public boolean isBookAvailableForRental(long bookId) {
        List<RentalDto> bookRentals = getRentalDataForBook(bookId);
        System.out.println(bookRentals.stream().noneMatch(rental -> rental.getRentalDate() != null));
            return bookRentals.stream().noneMatch(rental -> rental.getReturnDate() == null);
    }

    public int getNumberOfActiveRentalsForUser(long userId) {
        return rentalRepository.findAllByUserId(userId).stream()
                .filter(rental -> rental.getReturnDate() == null)
                .toList()
                .size();
    }

    public List<BookDto> getMostPopularBooks(int limit) {
        return List.of();
    }


    public List<UserDto> getMostActiveUsers(int limit) {
        return List.of();
    }

    public RentalStatisticsDto getRentalStatistics() {
        return null;
    }
}
