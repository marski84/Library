package org.localhost.library.library.services.RentalOperationsGateway;

import org.localhost.library.book.BookService;
import org.localhost.library.book.model.Book;
import org.localhost.library.library.RentalStatus;
import org.localhost.library.user.UserService;
import org.localhost.library.user.model.User;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class BaseRentalOperationsGateway implements RentalOperationsGateway {
    private final BookService bookService;
    private final UserService userService;

    private final int OVERDUE = -1;
    private final int DUE_TODAY = 0;
    private final int ON_TIME = 1;

    public BaseRentalOperationsGateway(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }


    @Override
    public Book getBookById(long id) {
        return bookService.getBookById(id);
    }

    @Override
    public User getUserById(long id) {
        return userService.findUserById(id);
    }

    @Override
    public void calculatePenaltyPoints(long userId, int maxPenaltyPoints, RentalStatus rentalStatus) {
        userService.updateUserPenaltyPoints(userId, maxPenaltyPoints, rentalStatus);
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