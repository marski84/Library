package org.localhost.library.library.controller.impl;

import org.junit.jupiter.api.Test;
import org.localhost.library.book.model.Book;
import org.localhost.library.book.service.BookService;
import org.localhost.library.library.services.CommandService.RentalCommandService;
import org.localhost.library.library.services.QueryService.RentalQueryService;
import org.localhost.library.user.model.User;
import org.localhost.library.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BaseLibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RentalQueryService rentalQueryService;

    @Autowired
    private RentalCommandService rentalCommandService;


    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    private final int TEST_USER_ID = 4;
    private final int BLOCKED_TEST_USER_ID = 3;
    private final int TEST_BOOK_ID = 6;

    @Test
    void rentBookToUserShouldReturn200() throws Exception {
        Book testBook = bookService.getBookById(TEST_BOOK_ID);
        User testUser = userService.findUserById(TEST_USER_ID);
//        when & then
        mockMvc.perform(get("/api/library/rent-book/{bookId}/{userId}",
                        TEST_BOOK_ID, TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(testBook.getIsbn()))
                .andExpect(jsonPath("$.bookId").value(testBook.getId()))
                .andExpect(jsonPath("$.userId").value(testUser.getId()))
                .andExpect(jsonPath("$.rentalDate").exists());
    }

    @Test
    void rentBookToUserShouldReturn400whenUserIsBlocked() throws Exception {
        String errorMessage = "Rental not possible";
        int errorCode = 1003;
        mockMvc.perform(get("/api/library/rent-book/{bookId}/{userId}",
                        TEST_BOOK_ID, BLOCKED_TEST_USER_ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.errorCode").value(errorCode));
    }
}