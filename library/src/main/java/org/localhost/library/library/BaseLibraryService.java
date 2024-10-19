package org.localhost.library.library;

import org.localhost.library.book.BookService;
import org.localhost.library.book.model.Book;
import org.localhost.library.config.BaseConfigService;
import org.localhost.library.config.ConfigService;
import org.localhost.library.library.ValueObjects.BookUserAssociation;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.exceptions.NotValidReturnDateException;
import org.localhost.library.library.exceptions.RentalNotFoundException;
import org.localhost.library.library.exceptions.RentalNotPossibleException;
import org.localhost.library.library.model.Rental;
import org.localhost.library.user.UserService;
import org.localhost.library.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class BaseLibraryService implements LibraryService {
    private final RentalRepository rentalRepository;
    private final BookService bookService;
    private final UserService userService;
    private final ConfigService baseConfigService;

    public BaseLibraryService(RentalRepository rentalRepository, BookService bookService, UserService userService, ConfigService baseConfigService) {
        this.rentalRepository = rentalRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.baseConfigService = baseConfigService;
    }

    @Override
    public SuccessfulRentalDto rentBookToUser(long bookId, long userId) {
        BookUserAssociation rentalData = new BookUserAssociation(bookId, userId);

        Book bookData = bookService.getBookById(rentalData.getBookId());
        User userData = userService.findUserById(rentalData.getUserId());
//        validate if book is available
        if (rentalRepository.findByBookIdAndRentDateIsEmpty(bookData.getId()).isPresent()) {
            throw new RentalNotPossibleException();
        }
//        validate if user is not blocked
        if (userData.isBlocked()) {
            throw new RentalNotPossibleException();
        }

        int rentalPeriod = baseConfigService.getRentalPeriodDays();

        Instant rentalDate = Instant.now();
        Instant dueDate = rentalDate.plus(rentalPeriod, ChronoUnit.DAYS);

        Rental rental = new Rental();
        rental.setBook(bookData);
        rental.setUser(userData);
        rental.setRentDate(rentalDate);
        rental.setDueDate(dueDate);

        Rental savedRentalData = rentalRepository.save(rental);

        return SuccessfulRentalDto.builder()
                .isbn(savedRentalData.getBook().getIsbn())
                .bookTitle(savedRentalData.getBook().getTitle())
                .author(savedRentalData.getBook().getAuthor())
                .userId(savedRentalData.getUser().getId())
                .rentalDate(savedRentalData.getRentDate())
                .dueDate(savedRentalData.getDueDate())
                .rentalTime((int) Duration.between(
                                savedRentalData.getRentDate(),
                                savedRentalData.getDueDate()
                        ).toDays()
                ).build();
    }

    @Override
    @Transactional
    public Rental registerBookReturn(long bookId, long userId, Instant returnDate) {
        BookUserAssociation rentalData = new BookUserAssociation(bookId, userId);

        Rental rental = rentalRepository.findRentalByBookIdAndUserId(rentalData.getBookId(), userId)
                .orElseThrow(RentalNotFoundException::new);

        if (rental.getRentDate().isAfter(returnDate)) {
            throw new NotValidReturnDateException();
        }

        rental.setReturnDate(returnDate);
        RentalStatus rentalStatus = checkRentalStatus(rental.getDueDate(), returnDate);

        int maxPenaltyPoints = baseConfigService.getMaxPenaltyPoints();

        switch (rentalStatus) {
            case DUE_TODAY, OVERDUE -> calculatePenaltyPoints(rentalData.getUserId(), maxPenaltyPoints, rentalStatus);
        }

        rentalRepository.save(rental);
        return rental;
    }

    @Transactional
    public void calculatePenaltyPoints(long userId, int maxPenaltyPoints, RentalStatus rentalStatus) {
        userService.updateUserPenaltyPoints(userId, maxPenaltyPoints, rentalStatus);

    }

    private RentalStatus checkRentalStatus(Instant dueDate, Instant returnDate) {
        return switch (dueDate.compareTo(returnDate)) {
            case -1 -> RentalStatus.OVERDUE;
            case 0 -> RentalStatus.DUE_TODAY;
            case 1 -> RentalStatus.ON_TIME;
            default -> throw new IllegalStateException("Unexpected comparison result");
        };
    }
}
