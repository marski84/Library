package org.localhost.library.library.services.CommandService.impl;

import org.localhost.library.book.model.Book;
import org.localhost.library.config.service.ConfigService;
import org.localhost.library.library.RentalStatus;
import org.localhost.library.library.ValueObjects.BookUserAssociation;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.exceptions.RentalException;
import org.localhost.library.library.exceptions.messages.RentalError;
import org.localhost.library.library.model.Rental;
import org.localhost.library.library.repository.RentalRepository;
import org.localhost.library.library.services.CommandService.RentalCommandService;
import org.localhost.library.library.services.RentalOperationsGateway.RentalOperationsGateway;
import org.localhost.library.user.model.User;
import org.localhost.library.utils.AppLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;

@Service
public class BaseRentalCommandService implements RentalCommandService {
    private static final Logger log = LoggerFactory.getLogger(BaseRentalCommandService.class);
    private final RentalRepository rentalRepository;
    private final RentalOperationsGateway rentalOperationsGateway;
    private final ConfigService baseConfigService;

    public BaseRentalCommandService(RentalRepository rentalRepository, RentalOperationsGateway rentalOperationsGateway, ConfigService baseConfigService) {
        this.rentalRepository = rentalRepository;
        this.rentalOperationsGateway = rentalOperationsGateway;
        this.baseConfigService = baseConfigService;
    }


    /**
     * Rents a book to a user.
     *
     * @param bookId The ID of the book to be rented
     * @param userId The ID of the user renting the book
     * @return SuccessfulRentalDto containing details of the rental
     * @throws RentalException if the book is unavailable or the user is blocked
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SuccessfulRentalDto rentBookToUser(long bookId, long userId) {
                BookUserAssociation rentalData = new BookUserAssociation(bookId, userId);

        Book bookData = rentalOperationsGateway.getBookById(rentalData.getBookId());
        User userData = rentalOperationsGateway.getUserById(rentalData.getUserId());

        validateBookAvailability(bookData);
        validateUserStatus(userData);

        int rentalPeriod = baseConfigService.getRentalPeriodDays();


        ZonedDateTime rentalDate = ZonedDateTime.now();
        ZonedDateTime dueDate = rentalDate.plusDays(rentalPeriod);

        Rental rental = createRental(bookData, userData, rentalDate, dueDate);

        Rental savedRentalData = rentalRepository.save(rental);
        AppLogger.logInfo("Rental saved: " + savedRentalData);

        AppLogger.logDebug("creating rental dto after successful rent: {}", rental.toString());
        return SuccessfulRentalDto.fromRental(savedRentalData);
    }

    private Rental createRental(Book book, User user, ZonedDateTime rentalDate, ZonedDateTime dueDate) {
        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.setRentDate(rentalDate);
        rental.setDueDate(dueDate);
        return rental;
    }

    /**
     * Registers a book return from user.
     *
     * @param bookId The ID of the book to be returned
     * @param userId The ID of the user returning the book
     * @return SuccessfulRentalDto containing details of the rental
     * @throws RentalException if the book is unavailable or the user is blocked
     * @throws RentalException if the return data is earlier than rent date
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Rental registerBookReturn(long bookId, long userId, ZonedDateTime returnDate) {
        BookUserAssociation rentalData = new BookUserAssociation(bookId, userId);

        System.out.println(rentalRepository.findRentalByBookIdAndUserId(rentalData.getBookId(), userId));

        Rental rental = rentalRepository.findRentalByBookIdAndUserId(rentalData.getBookId(), userId)
                .orElseThrow(() -> {
                            RentalException notFoundException = new RentalException(RentalError.RENTAL_NOT_FOUND);
                            AppLogger.logError(notFoundException.getErrorCode() + ": " + rentalData.getBookId());
                            return notFoundException;
                        }
                );


        if (rental.getRentDate().isAfter(returnDate)) {
            RentalException rentalException = new RentalException(RentalError.NOT_VALID_RETURN_DATE);
            AppLogger.logError(rentalException.getErrorCode() + ": " + rentalData.getBookId());
            throw rentalException;
        }

        rental.setReturnDate(returnDate);
        rental.setRentDate(null);
        RentalStatus rentalStatus = rentalOperationsGateway.checkRentalStatus(rental.getDueDate(), rental.getReturnDate());

        System.out.println(rental.getReturnDate());
        if (rentalStatus == RentalStatus.DUE_TODAY || rentalStatus == RentalStatus.OVERDUE) {
            rentalOperationsGateway.calculatePenaltyPoints(userId, rentalStatus);
        }

        AppLogger.logInfo("Rental saved: " + rental);
        return rentalRepository.save(rental);
    }


    public ZonedDateTime extendRental(long rentalId, int days) throws RentalException {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> {
                            RentalException rentalException = new RentalException(RentalError.RENTAL_NOT_FOUND);
                            AppLogger.logError(rentalException.getErrorCode() + ": " + rentalId);
                            return rentalException;
                        }
                );
        ZonedDateTime currentDate = ZonedDateTime.now();
        if (currentDate.isBefore(rental.getDueDate())) {
            rental.setDueDate(rental.getDueDate().plusDays(days));
            Rental extendedRental = rentalRepository.save(rental);

            AppLogger.logInfo("Rental extended: " + extendedRental);
            return extendedRental.getDueDate();
        }
        else {
            RentalException rentalException = new RentalException(RentalError.RENTAL_NOT_EXTENDABLE);
            AppLogger.logError(rentalException.getErrorCode() + ": " + rentalId);
            throw rentalException;
        }
    }



    private void validateBookAvailability(Book book) {
        if (rentalRepository.findByBookIdAndRentDateIsNotNull(book.getId()).isPresent()) {
            System.out.println("wyjatek");
            RentalException rentalException = new RentalException(RentalError.RENTAL_NOT_POSSIBLE);
            AppLogger.logError(rentalException.getErrorCode() + ": " + book.getId());
            throw rentalException;
        }
    }

    private void validateUserStatus(User user) {
        if (user.isBlocked()) {
            RentalException rentalException = new RentalException(RentalError.RENTAL_NOT_POSSIBLE);
            AppLogger.logError(rentalException.getErrorCode() + "for: " + user.getId());
            throw rentalException;
        }
    }



}
