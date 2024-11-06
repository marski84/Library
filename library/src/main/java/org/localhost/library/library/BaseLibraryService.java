package org.localhost.library.library;

import org.localhost.library.book.BookService;
import org.localhost.library.book.dto.BookDto;
import org.localhost.library.book.model.Book;
import org.localhost.library.config.ConfigService;
import org.localhost.library.library.ValueObjects.BookUserAssociation;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.dto.RentalStatisticsDto;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.exceptions.RentalException;
import org.localhost.library.library.exceptions.messages.RentalError;
import org.localhost.library.library.model.Rental;
import org.localhost.library.user.UserService;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.model.User;
import org.localhost.library.utils.AppLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BaseLibraryService implements LibraryService {
    private final RentalRepository rentalRepository;
    private final BookService bookService;
    private final UserService userService;
    private final ConfigService baseConfigService;

    private final int OVERDUE = -1;
    private final int DUE_TODAY = 0;
    private final int ON_TIME = 1;

    public BaseLibraryService(RentalRepository rentalRepository, BookService bookService, UserService userService, ConfigService baseConfigService) {
        this.rentalRepository = rentalRepository;
        this.bookService = bookService;
        this.userService = userService;
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

        Book bookData = bookService.getBookById(rentalData.getBookId());
        User userData = userService.findUserById(rentalData.getUserId());

        validateBookAvailability(bookData);
        validateUserStatus(userData);

        int rentalPeriod = baseConfigService.getRentalPeriodDays();

        ZonedDateTime rentalDate = ZonedDateTime.now();
        ZonedDateTime dueDate = rentalDate.plusDays(rentalPeriod);

        Rental rental = createRental(bookData, userData, rentalDate, dueDate);

        Rental savedRentalData = rentalRepository.save(rental);
        AppLogger.logInfo("Rental saved: " + savedRentalData);

        AppLogger.logDebug("creating rental dto after successful rent: {}", rental.toString());
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
     * @throws RentalException  if the book is unavailable or the user is blocked
     * @throws RentalException if the return data is earlier than rent date
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Rental registerBookReturn(long bookId, long userId, ZonedDateTime returnDate) {
        BookUserAssociation rentalData = new BookUserAssociation(bookId, userId);

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
        RentalStatus rentalStatus = checkRentalStatus(rental.getDueDate(), returnDate);

        int maxPenaltyPoints = baseConfigService.getMaxPenaltyPoints();

        if (rentalStatus == RentalStatus.DUE_TODAY || rentalStatus == RentalStatus.OVERDUE) {
            calculatePenaltyPoints(rentalData.getUserId(), maxPenaltyPoints, rentalStatus);
        }

        rentalRepository.save(rental);
        AppLogger.logInfo("Rental saved: " + rental);
        return rental;
    }

    private void validateBookAvailability(Book book) {
        if (rentalRepository.findByBookIdAndRentDateIsEmpty(book.getId()).isPresent()) {
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

    public void calculatePenaltyPoints(long userId, int maxPenaltyPoints, RentalStatus rentalStatus) {
        userService.updateUserPenaltyPoints(userId, maxPenaltyPoints, rentalStatus);
    }

    @Override
    public List<RentalDto> getRentalDataForBook(long bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book id must be greater than zero");
        }
        List<Rental> rentalList = rentalRepository.findAllByBookId(bookId);
        return rentalList.stream()
                .map(this::createRentalDto)
                .toList();
    }

    @Override
    public List<RentalDto> getRentalDataForUser(long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User id must be greater than zero");
        }
        List<Rental> rentalList = rentalRepository.findAllByUserId(userId);
        return rentalList.stream()
                .map(this::createRentalDto)
                .toList();
    }

    @Override
    public List<RentalDto> getActiveRentals() {
        List<Rental> activeRentals = rentalRepository.findAllByReturnDateIsNull();
        if (activeRentals.isEmpty()) {
            return List.of();
        }
        return activeRentals.stream()
                .map(this::createRentalDto)
                .toList();
    }

    @Override
    public List<RentalDto> getOverdueRentals() {
        return rentalRepository.findOverdueRentals(ZonedDateTime.now()).stream()
                .map(this::createRentalDto)
                .toList();
    }

    @Override
    public RentalDto getCurrentRentalForBook(long bookId) {
        return rentalRepository.findByBookId(bookId)
                .map(this::createRentalDto)
                .orElseThrow(() -> {
                            RentalException rentalException = new RentalException(RentalError.RENTAL_NOT_FOUND);
                            AppLogger.logError(rentalException.getErrorCode() + ": " + bookId);
                            return rentalException;
                        }
                );
    }

    @Override
    public boolean isBookAvailableForRental(long bookId) {
        return false;
    }

    @Override
    public int getNumberOfActiveRentalsForUser(long userId) {
        return 0;
    }

    @Override
    public List<BookDto> getMostPopularBooks(int limit) {
        return List.of();
    }

    @Override
    public List<UserDto> getMostActiveUsers(int limit) {
        return List.of();
    }

    @Override
    public RentalStatisticsDto getRentalStatistics() {
        return null;
    }

    @Override
    public void extendRental(long rentalId, int days) throws RentalException {

    }

    private RentalDto createRentalDto(Rental rental) {
        AppLogger.logDebug("creating rental dto for {}", rental.toString());
        return RentalDto.builder()
                .bookTitle(rental.getBook().getTitle())
                .author(rental.getBook().getAuthor())
                .isbn(rental.getBook().getIsbn())
                .userId(rental.getUser().getId())
                .rentalDate(rental.getRentDate())
                .rentalTime((int) Duration.between(
                        rental.getRentDate(),
                        rental.getDueDate()
                ).toDays())
                .dueDate(rental.getDueDate())
                .build();

    }

    private RentalStatus checkRentalStatus(ZonedDateTime dueDate, ZonedDateTime returnDate) {
        return switch (dueDate.compareTo(returnDate)) {
            case OVERDUE -> RentalStatus.OVERDUE;
            case DUE_TODAY -> RentalStatus.DUE_TODAY;
            case ON_TIME -> RentalStatus.ON_TIME;
            default -> throw new IllegalStateException("Unexpected comparison result");
        };
    }
}
