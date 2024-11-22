package org.localhost.library.library.controller.impl;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.localhost.library.book.service.BookService;
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
    private final int RENTED_BOOK_ID = 1;
    private final int RENTED_USER_ID = 1;
    private final int NON_EXTENDABLE_TEST_RENTAL_ID = 2;
    private final int NON_EXISTING_RENTAL_ID = 100;


    @Test
    void getRentalDataForBookShouldReturn200() throws Exception {
        int EXPECTED_RENTALS_SIZE = rentalRepository.findAllByBookId(RENTED_BOOK_ID).size();
        mockMvc.perform(get("/api/library/rentals/book/{bookId}", RENTED_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(EXPECTED_RENTALS_SIZE)));
    }
}
