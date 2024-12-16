package org.localhost.library.library.controller.impl;

import org.junit.jupiter.api.*;
import org.localhost.library.book.model.Book;
import org.localhost.library.book.service.BookService;
import org.localhost.library.library.services.CommandService.RentalCommandService;
import org.localhost.library.library.services.QueryService.RentalQueryService;
import org.localhost.library.user.model.User;
import org.localhost.library.user.service.UserService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class RentalCommandIntegrationTests {

    private MockMvc mockMvc;
    private RentalQueryService rentalQueryService;
    private RentalCommandService rentalCommandService;
    private BookService bookService;
    private UserService userService;

    private final int TEST_USER_ID = 4;
    private final int BLOCKED_TEST_USER_ID = 3;
    private final int TEST_BOOK_ID = 6;
    private final int RENTED_BOOK_ID = 1;
    private final int RENTED_USER_ID = 1;
    private final int NON_EXTENDABLE_TEST_RENTAL_ID = 2;
    private final int NON_EXISTING_RENTAL_ID = 100;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.rentalQueryService = webApplicationContext.getBean(RentalQueryService.class);
        this.rentalCommandService = webApplicationContext.getBean(RentalCommandService.class);
        this.bookService = webApplicationContext.getBean(BookService.class);
        this.userService = webApplicationContext.getBean(UserService.class);
    }

    @Test
    @Order(1)
    void rentBookToUserShouldReturn200() throws Exception {
        Book testBook = bookService.getBookById(TEST_BOOK_ID);
        User testUser = userService.findUserById(TEST_USER_ID);

        mockMvc.perform(post("/api/library/rent-book/{bookId}/{userId}",
                        TEST_BOOK_ID, TEST_USER_ID))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value(testBook.getIsbn()))
                .andExpect(jsonPath("$.bookId").value(testBook.getId()))
                .andExpect(jsonPath("$.userId").value(testUser.getId()))
                .andExpect(jsonPath("$.rentalDate").exists());
    }

    @Test
    @Order(2)
    void rentBookToUserShouldReturn400whenUserIsBlocked() throws Exception {
        String errorMessage = "Rental not possible";
        int errorCode = 1003;

        mockMvc.perform(post("/api/library/rent-book/{bookId}/{userId}",
                        TEST_BOOK_ID, BLOCKED_TEST_USER_ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.errorCode").value(errorCode));
    }

    @Test
    @Order(3)
    void returnBookShouldReturn200() throws Exception {
        mockMvc.perform(patch("/api/library/return-book/{bookId}/{userId}",
                        RENTED_BOOK_ID, RENTED_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.id").value(RENTED_BOOK_ID))
                .andExpect(jsonPath("$.user.id").value(RENTED_USER_ID))
                .andExpect(jsonPath("$.returnDate").exists());
    }

    @Test
    @Order(4)
    void returnBookShouldReturn400WhenBookWasNotRented() throws Exception {
        String errorMessage = "Rental not found";
        int errorCode = 1002;

        mockMvc.perform(patch("/api/library/return-book/{bookId}/{userId}",
                        TEST_BOOK_ID, TEST_USER_ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.errorCode").value(errorCode));
    }

    @Test
    @Order(5)
    void extendRentalShouldReturn400whenRentalNotExtenable() throws Exception {
        String errorMessage = "Rental not extendable";
        int errorCode = 1004;
        int days = 5;

        mockMvc.perform(patch("/api/library/extend-rental/{rentalId}/{days}",
                        NON_EXTENDABLE_TEST_RENTAL_ID, days))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.errorCode").value(errorCode));
    }

    @Test
    @Order(6)
    void extendRentalShouldReturn200() throws Exception {
        String errorMessage = "Rental not extendable";
        int errorCode = 1004;
        int days = 5;

        mockMvc.perform(patch("/api/library/extend-rental/{rentalId}/{days}",
                        NON_EXTENDABLE_TEST_RENTAL_ID, days))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.errorCode").value(errorCode));
    }

    @Test
    void extendRentalShouldReturn400whenRentalNotFound() throws Exception {
        String errorMessage = "Rental not found";
        int errorCode = 1002;
        int days = 5;

        mockMvc.perform(patch("/api/library/extend-rental/{rentalId}/{days}",
                        NON_EXISTING_RENTAL_ID, days))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.errorCode").value(errorCode));
    }
}