package org.localhost.library.library.services.CommandService;

import org.localhost.library.library.RentalStatus;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.exceptions.RentalException;
import org.localhost.library.library.model.Rental;

import java.time.ZonedDateTime;

public interface RentalCommandService {
    SuccessfulRentalDto rentBookToUser(long bookId, long userId);

    Rental registerBookReturn(long bookId, long userId, ZonedDateTime returnDate);
    ZonedDateTime extendRental(long rentalId, int days) throws RentalException;


}
