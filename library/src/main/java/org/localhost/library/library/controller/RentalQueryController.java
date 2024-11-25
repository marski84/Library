package org.localhost.library.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.localhost.library.book.dto.BookDto;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.model.User;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface RentalQueryController {
    @GetMapping("/book/{bookId}")
    @Operation(summary = "Get rentals for book", description = "Returns all rentals for specified book")
    @ApiResponse(responseCode = "200", description = "Found rentals")
    @ApiResponse(responseCode = "404", description = "Book not found")
    ResponseEntity<List<RentalDto>> getRentalDataForBook(
            @PathVariable @Positive(message = "Book ID must be positive")
            @NotNull(message = "Book ID cannot be null")
            Long bookId);

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get rentals for user", description = "Returns all rentals for specified user")
    @ApiResponse(responseCode = "200", description = "Found rentals")
    @ApiResponse(responseCode = "404", description = "User not found")
    ResponseEntity<List<RentalDto>> getRentalDataForUser(
            @PathVariable
            @Positive(message = "User ID must be positive")
            @NotNull(message = "User ID cannot be null")
            Long userId);

    @GetMapping("/active")
    @Operation(summary = "Get active rentals", description = "Returns all active rentals")
    @ApiResponse(responseCode = "200", description = "Found active rentals")
    ResponseEntity<List<RentalDto>> getActiveRentals();

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue rentals", description = "Returns all overdue rentals")
    @ApiResponse(responseCode = "200", description = "Found overdue rentals")
    ResponseEntity<List<RentalDto>> getOverdueRentals();


    @GetMapping("/book/{bookId}/active")
    @Operation(summary = "Get active rental for book", description = "Returns active rental for specified book")
    @ApiResponse(responseCode = "200", description = "Found active rental")
    @ApiResponse(responseCode = "404", description = "Book not found")
    ResponseEntity<RentalDto> getActiveRentalForBook(
            @PathVariable
            @Positive(message = "Book ID must be positive")
            @NotNull(message = "Book ID cannot be null")
            Long bookId);

    @GetMapping("/book/{bookId}/available")
    @Operation(summary = "Check if book is available for rental",
            description = "Returns true if book is available for rental")
    @ApiResponse(responseCode = "200", description = "Book is available for rental")
    @ApiResponse(responseCode = "404", description = "Book not found")
    ResponseEntity<Boolean> isBookAvailableForRental(
            @PathVariable
            @Positive(message = "Book ID must be positive")
            @NotNull(message = "Book ID cannot be null")
            Long bookId);


    @GetMapping("/user/{userId}/active")
    @Operation(summary = "Get number of active rentals for user",
            description = "Returns number of active rentals for specified user")
    @ApiResponse(responseCode = "200", description = "Found active rentals")
    @ApiResponse(responseCode = "404", description = "User not found")
    ResponseEntity<Integer> getNumberOfActiveRentalsForUser(
            @PathVariable
            @Positive(message = "User ID must be positive")
            @NotNull(message = "User ID connot be null")
            Long userId);


    @GetMapping("/most-popular-books/{limit}")
    ResponseEntity<List<BookDto>> getMostPopularBooks(
            @PathVariable
            @Positive(message = "Limit must be positive")
            @NotNull(message = "Limit cannot be null")
            Integer limit);

    @GetMapping("/most-active-users/{limit}")
    ResponseEntity<List<User>> getMostActiveUsers(
            @PathVariable
            @Positive(message = "Limit must be positive")
            @NotNull(message = "Limit cannot be null")
            Integer limit);
}

