package org.localhost.library.library.controller.impl;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.localhost.library.book.service.BookService;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.repository.RentalRepository;
import org.localhost.library.library.services.CommandService.RentalCommandService;
import org.localhost.library.library.services.QueryService.RentalQueryService;
import org.localhost.library.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class RentalQueryIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RentalQueryService rentalQueryService;

    @Autowired
    private RentalCommandService rentalCommandService;

    @Autowired
    private RentalRepository rentalRepository;


    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    private final int TEST_USER_ID = 4;
    private final int BLOCKED_TEST_USER_ID = 3;
    private final int TEST_BOOK_ID = 6;
    private final int BOOK_ID_WITH_ACTIVE_RENTAL = 1;
    private final int USER_WITH_ACTIVE_RENTAL = 1;
    private final int NON_EXTENDABLE_TEST_RENTAL_ID = 2;
    private final int NON_EXISTING_RENTAL_ID = 100;
    private final int BOOK_ID_WITH_FINISHED_RENTAL = 3;
    private final int NO_RENTALS_FOUND = 0;


    @Test
    void getRentalDataForBookShouldReturn200AndNotEmptyList() throws Exception {
        int EXPECTED_RENTALS_SIZE = rentalRepository.findAllByBookId(BOOK_ID_WITH_ACTIVE_RENTAL).size();
        mockMvc.perform(get("/api/library/rentals/book/{bookId}", BOOK_ID_WITH_ACTIVE_RENTAL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_RENTALS_SIZE)));
    }

    @Test
    void getRentalDataForBookShouldReturn200AndEmptyList() throws Exception {
        int EXPECTED_RENTALS_SIZE = rentalRepository.findAllByBookId(NON_EXISTING_RENTAL_ID).size();
        mockMvc.perform(get("/api/library/rentals/book/{bookId}", NON_EXISTING_RENTAL_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_RENTALS_SIZE)));
    }

    @Test
    void getRentalDataForUserShouldReturn200AndNotEmptyList() throws Exception {
        int EXPECTED_RENTALS_SIZE = rentalRepository.findAllByUserId(USER_WITH_ACTIVE_RENTAL).size();
        mockMvc.perform(get("/api/library/rentals/user/{userId}", USER_WITH_ACTIVE_RENTAL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_RENTALS_SIZE)));
    }

    @Test
    void getRentalDataForUserShouldReturn200AndEmptyList() throws Exception {
        int EXPECTED_RENTALS_SIZE = rentalRepository.findAllByUserId(NON_EXISTING_RENTAL_ID).size();
        mockMvc.perform(get("/api/library/rentals/user/{userId}", NON_EXISTING_RENTAL_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_RENTALS_SIZE)));
    }

    @Test
    void getActiveRentalsShouldReturn200AndNotEmptyList() throws Exception {
        int EXPECTED_RENTALS_SIZE = rentalRepository.findAllByReturnDateIsNull().size();
        mockMvc.perform(get("/api/library/rentals/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_RENTALS_SIZE)));
    }


    @Test
    void getOverdueRentalsShouldReturn200AndNotEmptyList() throws Exception {
        int EXPECTED_RENTALS_SIZE = rentalRepository.findOverdueRentals(ZonedDateTime.now()).size();
        mockMvc.perform(get("/api/library/rentals/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_RENTALS_SIZE)));
    }

    @Test
    void getActiveRentalForBookShouldReturn200() throws Exception {
//        given
        RentalDto testRental = rentalQueryService.getRentalDataForBook(BOOK_ID_WITH_ACTIVE_RENTAL).stream()
                .filter(rental -> rental.getReturnDate() == null)
                .toList().get(0);
//        when&then
        mockMvc.perform(get("/api/library/rentals/book/{bookId}/active", BOOK_ID_WITH_ACTIVE_RENTAL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testRental.getId()))
                .andExpect(jsonPath("$.bookId").value(testRental.getBookId()))
                .andExpect(jsonPath("$.userId").value(testRental.getUserId()))
                .andExpect(jsonPath("$.returnDate").doesNotExist());
    }

    @Test
    void isBookAvailableForRentalShouldReturn200AndTrue() throws Exception {
        mockMvc.perform(get("/api/library/rentals/book/{bookId}/available", BOOK_ID_WITH_FINISHED_RENTAL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void isBookAvailableForRentalShouldReturn200AndFalse() throws Exception {
        mockMvc.perform(get("/api/library/rentals/book/{bookId}/available", BOOK_ID_WITH_ACTIVE_RENTAL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    void isBookAvailableForRentalShouldReturn400WhenRentalNotFound() throws Exception {
        String RENTAL_NOT_FOUND = "Rental not found";
        int ERROR_CODE = 1002;
        mockMvc.perform(get("/api/library/rentals/book/{bookId}/available", NON_EXISTING_RENTAL_ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(RENTAL_NOT_FOUND))
                .andExpect(jsonPath("$.errorCode").value(ERROR_CODE));
    }

    @Test
    void getNumberOfActiveRentalsForUserShouldReturn200AndRentalAmountShouldBeMoreThanZero() throws Exception {
        int EXPECTED_RENTALS_SIZE = rentalRepository.findAllByUserId(USER_WITH_ACTIVE_RENTAL).stream()
                .filter(rental -> rental.getReturnDate() == null)
                .toList()
                .size();
        mockMvc.perform(get("/api/library/rentals/user/{userId}/active", USER_WITH_ACTIVE_RENTAL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(EXPECTED_RENTALS_SIZE));
    }

    @Test
    void getNumberOfActiveRentalsForUserShouldReturn200AndRentalAmountShouldBeZero() throws Exception {
        mockMvc.perform(get("/api/library/rentals/user/{userId}/active", NON_EXISTING_RENTAL_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(NO_RENTALS_FOUND));
    }

    @Test
    void getMostPopularBooksShouldReturn200AndNotEmptyList() throws Exception {
        mockMvc.perform(get("/api/library/rentals/most-popular-books/{limit}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getMostActiveUsersShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/library/rentals/most-active-users/{limit}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
    }


}
