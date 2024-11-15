package org.localhost.library.library.services.RentalOperationsGateway.impl;

import org.localhost.library.book.model.Book;
import org.localhost.library.book.service.BookService;
import org.localhost.library.library.RentalStatus;
import org.localhost.library.library.services.RentalOperationsGateway.RentalOperationsGateway;
import org.localhost.library.user.model.User;
import org.localhost.library.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class BaseRentalOperationsGateway implements RentalOperationsGateway {
    private final BookService bookService;
    private final UserService userService;


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
    public void calculatePenaltyPoints(long userId, RentalStatus rentalStatus) {
        userService.updateUserPenaltyPoints(userId, rentalStatus);
    }

    public RentalStatus checkRentalStatus(ZonedDateTime dueDate, ZonedDateTime returnDate) {
        final int OVERDUE = -1;
        final int DUE_TODAY = 0;
        final int ON_TIME = 1;

        return switch (dueDate.compareTo(returnDate)) {
            case OVERDUE -> RentalStatus.OVERDUE;
            case DUE_TODAY -> RentalStatus.DUE_TODAY;
            case ON_TIME -> RentalStatus.ON_TIME;
            default -> throw new IllegalStateException("Unexpected comparison result");
        };
    }
}
