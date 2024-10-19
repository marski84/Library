package org.localhost.library.library;

import org.localhost.library.book.dto.BookDto;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.model.Rental;

import java.time.Instant;

public interface LibraryService {

    SuccessfulRentalDto rentBookToUser(long bookId, long userId);
    Rental registerBookReturn(long bookId, long userId, Instant returnDate);
}
