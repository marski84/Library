package org.localhost.library.library.controller.impl;

import org.localhost.library.book.dto.BookDto;
import org.localhost.library.library.controller.RentalQueryController;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.services.QueryService.RentalQueryService;
import org.localhost.library.library.services.RentalOperationsGateway.RentalOperationsGateway;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.model.User;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping("/api/library/rentals")
@Validated
public class BaseRentalQueryController implements RentalQueryController {
    private final RentalOperationsGateway rentalOperationsGateway;
    private final RentalQueryService rentalQueryService;

    public BaseRentalQueryController(RentalOperationsGateway rentalOperationsGateway, RentalQueryService rentalQueryService) {
        this.rentalOperationsGateway = rentalOperationsGateway;
        this.rentalQueryService = rentalQueryService;
    }

    @Override
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<RentalDto>> getRentalDataForBook(Long bookId) {
        List<RentalDto> rentalsList = rentalQueryService.getRentalDataForBook(bookId);
        return ResponseEntity.ok(rentalsList);
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalDto>> getRentalDataForUser(Long userId) {
        List<RentalDto> rentalsList = rentalQueryService.getRentalDataForBook(userId);
        return ResponseEntity.ok(rentalsList);
    }

    @Override
    @GetMapping("/active")
    public ResponseEntity<List<RentalDto>> getActiveRentals() {
        List<RentalDto> rentalsList = rentalQueryService.getActiveRentals();
        return ResponseEntity.ok(rentalsList);
    }

    @Override
    @GetMapping("/overdue")
    public ResponseEntity<List<RentalDto>> getOverdueRentals() {
        List<RentalDto> rentalList = rentalQueryService.getOverdueRentals();
        return ResponseEntity.ok(rentalList);
    }

    @Override
    @GetMapping("/book/{bookId}/active")
    public ResponseEntity<RentalDto> getActiveRentalForBook(@PathVariable Long bookId) {
        RentalDto rental = rentalQueryService.getCurrentRentalForBook(bookId);
        return ResponseEntity.ok(rental);
    }

    @Override
    @GetMapping("/book/{bookId}/available")
    public ResponseEntity<Boolean> isBookAvailableForRental(@PathVariable Long bookId) {
        return rentalQueryService.isBookAvailableForRental(bookId) ?
                ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }


    @Override
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<Integer> getNumberOfActiveRentalsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(rentalQueryService.getNumberOfActiveRentalsForUser(userId));
    }

    @Override
    @GetMapping("/most-popular-books/{limit}")
    public ResponseEntity<List<BookDto>> getMostPopularBooks(@PathVariable Integer limit) {
        return ResponseEntity.ok(rentalQueryService.getMostPopularBooks(limit));
    }

    @Override
    public ResponseEntity<List<User>> getMostActiveUsers(Integer limit) {
        return ResponseEntity.ok(rentalQueryService.getMostActiveUsers(limit));
    }


}
