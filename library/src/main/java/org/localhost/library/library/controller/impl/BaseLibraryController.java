package org.localhost.library.library.controller.impl;

import org.localhost.library.library.controller.LibraryController;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.model.Rental;
import org.localhost.library.library.services.CommandService.RentalCommandService;
import org.localhost.library.library.services.QueryService.RentalQueryService;
import org.localhost.library.library.services.RentalOperationsGateway.RentalOperationsGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/library")
public class BaseLibraryController implements LibraryController {
    private final RentalOperationsGateway rentalOperationsGateway;
    private final RentalQueryService rentalQueryService;
    private final RentalCommandService rentalCommandService;

    public BaseLibraryController(RentalOperationsGateway rentalOperationsGateway, RentalQueryService rentalQueryService, RentalCommandService rentalCommandService) {
        this.rentalOperationsGateway = rentalOperationsGateway;
        this.rentalQueryService = rentalQueryService;
        this.rentalCommandService = rentalCommandService;
    }

    @GetMapping("/rent-book/{bookId}/{userId}")
    public ResponseEntity<SuccessfulRentalDto> rentBookToUser(@PathVariable long bookId, @PathVariable long userId) {
        SuccessfulRentalDto rental = rentalCommandService.rentBookToUser(bookId, userId);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/return-book/{bookId}/{userId}")
    public ResponseEntity<Rental> returnBook(@PathVariable long bookId, @PathVariable long userId) {
        ZonedDateTime returnDate = ZonedDateTime.now();
        Rental BookReturn = rentalCommandService.registerBookReturn(bookId, userId, returnDate);
        return ResponseEntity.ok(BookReturn);
    }


}
