package org.localhost.library.library.controller.impl;

import org.localhost.library.library.controller.RentalQueryController;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.services.QueryService.RentalQueryService;
import org.localhost.library.library.services.RentalOperationsGateway.RentalOperationsGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping("/api/library/rentals")
public class BaseRentalQueryController implements RentalQueryController {
    private final RentalOperationsGateway rentalOperationsGateway;
    private final RentalQueryService rentalQueryService;

    public BaseRentalQueryController(RentalOperationsGateway rentalOperationsGateway, RentalQueryService rentalQueryService) {
        this.rentalOperationsGateway = rentalOperationsGateway;
        this.rentalQueryService = rentalQueryService;
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<RentalDto>> getRentalDataForBook(@PathVariable long bookId) {
        List<RentalDto> rentalsList = rentalQueryService.getRentalDataForBook(bookId);
        rentalsList.forEach(System.out::println);
        return ResponseEntity.ok(rentalsList);
    }

    @Override
    public ResponseEntity<List<RentalDto>> getRentalDataForUser(@PathVariable long userId) {
        List<RentalDto> rentalsList = rentalQueryService.getRentalDataForBook(userId);
        return ResponseEntity.ok(rentalsList);}
}
