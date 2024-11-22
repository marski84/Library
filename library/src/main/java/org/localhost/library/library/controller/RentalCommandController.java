package org.localhost.library.library.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.model.Rental;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.ZonedDateTime;

public interface RentalCommandController {
    ResponseEntity<SuccessfulRentalDto> rentBookToUser(@PathVariable @NotNull @Positive long bookId,
                                                       @PathVariable @NotNull @Positive long userId);

    ResponseEntity<Rental> returnBook(@PathVariable @NotNull @Positive long bookId,
                                      @PathVariable @NotNull @Positive long userId);

    ResponseEntity<ZonedDateTime> extendRental(@PathVariable @NotNull @Positive long rentalId,
                                               @PathVariable @NotNull @Positive int days);
}
