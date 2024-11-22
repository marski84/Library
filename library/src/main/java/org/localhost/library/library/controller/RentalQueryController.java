package org.localhost.library.library.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.localhost.library.library.dto.RentalDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface RentalQueryController {
    ResponseEntity<List<RentalDto>> getRentalDataForBook(@Positive @NotNull long bookId);
    ResponseEntity<List<RentalDto>> getRentalDataForUser(long userId);
}
