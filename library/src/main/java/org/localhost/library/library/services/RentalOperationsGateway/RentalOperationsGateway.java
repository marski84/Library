package org.localhost.library.library.services.RentalOperationsGateway;

import org.localhost.library.book.model.Book;
import org.localhost.library.library.RentalStatus;
import org.localhost.library.user.model.User;

public interface RentalOperationsGateway {
    Book getBookById(long id);

    User getUserById(long id);

    void calculatePenaltyPoints(long userId, RentalStatus rentalStatus);
}
