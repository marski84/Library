package org.localhost.library.library.controller.impl;

import org.localhost.library.library.controller.RentalCommandController;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.model.Rental;
import org.localhost.library.library.services.CommandService.RentalCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@Validated
@RequestMapping("/api/library")
public class BaseRentalCommandController implements RentalCommandController {
    private final RentalCommandService rentalCommandService;

    public BaseRentalCommandController(RentalCommandService rentalCommandService) {
        this.rentalCommandService = rentalCommandService;
    }

    @PostMapping("/rent-book/{bookId}/{userId}")
    public ResponseEntity<SuccessfulRentalDto> rentBookToUser(@PathVariable long bookId, @PathVariable long userId) {
        SuccessfulRentalDto rental = rentalCommandService.rentBookToUser(bookId, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rental);
    }

    @PatchMapping("/return-book/{bookId}/{userId}")
    public ResponseEntity<Rental> returnBook(@PathVariable long bookId, @PathVariable long userId) {
        ZonedDateTime returnDate = ZonedDateTime.now();
        Rental BookReturn = rentalCommandService.registerBookReturn(bookId, userId, returnDate);
        return ResponseEntity.ok(BookReturn);
    }

    @PatchMapping("/extend-rental/{rentalId}/{days}")
    public ResponseEntity<ZonedDateTime> extendRental(@PathVariable long rentalId, @PathVariable int days) {
        ZonedDateTime newReturnDate = rentalCommandService.extendRental(rentalId, days);
        return ResponseEntity.ok(newReturnDate);
    }


}
