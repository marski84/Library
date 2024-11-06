package org.localhost.library.library.services.QueryService;

import org.localhost.library.book.BookService;
import org.localhost.library.book.dto.BookDto;
import org.localhost.library.book.model.Book;
import org.localhost.library.library.RentalRepository;
import org.localhost.library.library.RentalStatus;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.dto.RentalStatisticsDto;
import org.localhost.library.library.exceptions.RentalException;
import org.localhost.library.library.exceptions.messages.RentalError;
import org.localhost.library.library.model.Rental;
import org.localhost.library.user.UserService;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.model.User;
import org.localhost.library.utils.AppLogger;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class BaseRentalQueryService implements RentalQueryService {
    private final RentalRepository rentalRepository;
    private final BookService bookService;
    private final UserService userService;

    private final int OVERDUE = -1;
    private final int DUE_TODAY = 0;
    private final int ON_TIME = 1;

    public BaseRentalQueryService(RentalRepository rentalRepository, BookService bookService, UserService userService) {
        this.rentalRepository = rentalRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public void validateBookAvailability(Book book) {
        if (rentalRepository.findByBookIdAndRentDateIsEmpty(book.getId()).isPresent()) {
            RentalException rentalException = new RentalException(RentalError.RENTAL_NOT_POSSIBLE);
            AppLogger.logError(rentalException.getErrorCode() + ": " + book.getId());
            throw rentalException;
        }
    }

    public void validateUserStatus(User user) {
        if (user.isBlocked()) {
            RentalException rentalException = new RentalException(RentalError.RENTAL_NOT_POSSIBLE);
            AppLogger.logError(rentalException.getErrorCode() + "for: " + user.getId());
            throw rentalException;
        }
    }

    public List<RentalDto> getRentalDataForBook(long bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book id must be greater than zero");
        }
        List<Rental> rentalList = rentalRepository.findAllByBookId(bookId);
        return rentalList.stream()
                .map(this::createRentalDto)
                .toList();
    }

    public List<RentalDto> getRentalDataForUser(long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User id must be greater than zero");
        }
        List<Rental> rentalList = rentalRepository.findAllByUserId(userId);
        return rentalList.stream()
                .map(this::createRentalDto)
                .toList();
    }

    public List<RentalDto> getActiveRentals() {
        List<Rental> activeRentals = rentalRepository.findAllByReturnDateIsNull();
        if (activeRentals.isEmpty()) {
            return List.of();
        }
        return activeRentals.stream()
                .map(this::createRentalDto)
                .toList();
    }

    public List<RentalDto> getOverdueRentals() {
        return rentalRepository.findOverdueRentals(ZonedDateTime.now()).stream()
                .map(this::createRentalDto)
                .toList();
    }

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

    public boolean isBookAvailableForRental(long bookId) {
        return false;
    }

    public int getNumberOfActiveRentalsForUser(long userId) {
        return 0;
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

    public RentalStatus checkRentalStatus(ZonedDateTime dueDate, ZonedDateTime returnDate) {
        return switch (dueDate.compareTo(returnDate)) {
            case OVERDUE -> RentalStatus.OVERDUE;
            case DUE_TODAY -> RentalStatus.DUE_TODAY;
            case ON_TIME -> RentalStatus.ON_TIME;
            default -> throw new IllegalStateException("Unexpected comparison result");
        };
    }
}
