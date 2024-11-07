package org.localhost.library.library.services.QueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.localhost.library.book.BaseBookService;
import org.localhost.library.book.BookRepository;
import org.localhost.library.book.BookService;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.model.Book;
import org.localhost.library.config.ConfigService;
import org.localhost.library.library.RentalRepository;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.services.CommandService.BaseRentalCommandService;
import org.localhost.library.library.services.InMemoryConfigService;
import org.localhost.library.library.services.RentalOperationsGateway.BaseRentalOperationsGateway;
import org.localhost.library.library.services.RentalOperationsGateway.RentalOperationsGateway;
import org.localhost.library.repositories.InMemoryBookRepository;
import org.localhost.library.repositories.InMemoryRentalRepository;
import org.localhost.library.repositories.InMemoryUserRepository;
import org.localhost.library.user.BaseUserService;
import org.localhost.library.user.UserRepository;
import org.localhost.library.user.UserService;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserRegistrationDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseRentalQueryServiceTest {


    private final String TEST_USER_NAME = "testName";
    private final String TEST_FIRST_NAME = "firstName";
    private final String TEST_LAST_NAME = "lastName";
    private final int TEST_AGE = 40;
    private final long NON_EXISTING_BOOK_ID = 4255L;
    private final long NON_EXISTING_USER_ID = 4255L;
    private final String EDITED_USER_NAME = "editedName";
    private int testRentalTime;

    private RentalQueryService objectUnderTest;
    private UserRegistrationDto testUserDto;
    private BookRegistrationDto bookRegistrationDto;

    private UserService userService;
    private BookService bookService;
    private ConfigService configService;
    private BaseRentalCommandService baseRentalCommandService;

    @BeforeEach
    void setUp() {
        RentalRepository InMemoryRepository = new InMemoryRentalRepository();
        UserRepository inMemoryUserRepository = new InMemoryUserRepository();
        BookRepository inMemoryBookrepository = new InMemoryBookRepository();
        RentalOperationsGateway rentalOperationsGateway = new BaseRentalOperationsGateway(bookService, userService);
        baseRentalCommandService = new BaseRentalCommandService(InMemoryRepository, rentalOperationsGateway, configService);


        userService = new BaseUserService(inMemoryUserRepository, configService);
        bookService = new BaseBookService(inMemoryBookrepository);
        configService = new InMemoryConfigService();

        objectUnderTest = new BaseRentalQueryService(InMemoryRepository, bookService, userService);

        testRentalTime = configService.getRentalPeriodDays();

        testUserDto = UserRegistrationDto.builder()
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .age(TEST_AGE)
                .build();

        bookRegistrationDto = BookRegistrationDto.builder()
                .title("Test book")
                .author("Test Author")
                .pages(398)
                .publisher("some publisher")
                .isbn("12412341")
                .build();

    }

    Book rehisterBook() {
        return bookService.registerBook(bookRegistrationDto);
    }

    RegisteredUserDto testUser() {
        return userService.registerUser(testUserDto);
    }


    @Test
    @DisplayName("getRentalDataForBook should return rental data for book id")
    void getRentalDataForBook() {
//        given
        Book testBook = rehisterBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = baseRentalCommandService.rentBookToUser(
                testBook.getId(), testUser.getId()
        );
//        when
        List<RentalDto> testResult = objectUnderTest.getRentalDataForBook(testBook.getId());

//        then

        assertAll(
                () -> assertEquals(testRental.getIsbn(), testResult.get(0).getIsbn()),
                () -> assertEquals(testRental.getAuthor(), testResult.get(0).getAuthor()),
                () -> assertEquals(testRental.getUserId(), testResult.get(0).getUserId()),
                () -> assertEquals(testRental.getRentalTime(), testResult.get(0).getRentalTime())
        );
    }

    @Test
    @DisplayName("getRentalDataForUser should return rentals for user")
    void getRentalDataForUser() {
//        given
        Book testBook = rehisterBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = baseRentalCommandService.rentBookToUser(
                testBook.getId(), testUser.getId());


        bookRegistrationDto = BookRegistrationDto.builder()
                .title("second")
                .author("second")
                .pages(555555)
                .publisher("test publisher")
                .isbn("987654321")
                .build();
        Book secondBook = bookService.registerBook(bookRegistrationDto);

        SuccessfulRentalDto secondTestRental = baseRentalCommandService.rentBookToUser(secondBook.getId(), testUser.getId());

        int expectedResultSize = List.of(testRental, secondTestRental).size();

//        when
        List<RentalDto> testResult = objectUnderTest.getRentalDataForUser(testUser.getId());
//        then
        assertAll(
                () -> assertEquals(expectedResultSize, testResult.size()),
                () -> assertEquals(testRental.getUserId(), testResult.get(0).getUserId()),
                () -> assertEquals(testRental.getIsbn(), testResult.get(0).getIsbn()),
                () -> assertEquals(secondTestRental.getUserId(), testResult.get(1).getUserId()),
                () -> assertEquals(secondTestRental.getIsbn(), testResult.get(1).getIsbn())
        );
    }

    @Test
    @DisplayName("getActiveRentals should return emppy list when no rentals are registerd")
    void getActiveRentals() {
//        when
        List<RentalDto> testResult = objectUnderTest.getActiveRentals();
//        then
        assertEquals(0, testResult.size());
    }

    @Test
    @DisplayName("getActiveRentals should return active rentals list")
    void getActiveRentalsList() {
//        given
        Book testBook = rehisterBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = baseRentalCommandService.rentBookToUser(
                testBook.getId(), testUser.getId());


        bookRegistrationDto = BookRegistrationDto.builder()
                .title("second")
                .author("second")
                .pages(555555)
                .publisher("test publisher")
                .isbn("987654321")
                .build();
        Book secondBook = bookService.registerBook(bookRegistrationDto);

        SuccessfulRentalDto secondTestRental = baseRentalCommandService.rentBookToUser(secondBook.getId(), testUser.getId());

        int expectedResultSize = List.of(testRental, secondTestRental).size();

//        when
        List<RentalDto> testResult = objectUnderTest.getRentalDataForUser(testUser.getId());
//        then
        assertEquals(expectedResultSize, testResult.size());
    }

    @Test
    void getOverdueRentals() {
    }

    @Test
    void getCurrentRentalForBook() {
    }

    @Test
    void isBookAvailableForRental() {
    }

    @Test
    void getNumberOfActiveRentalsForUser() {
    }

    @Test
    void getMostPopularBooks() {
    }

    @Test
    void getMostActiveUsers() {
    }

    @Test
    void getRentalStatistics() {
    }

    @Test
    void extendRental() {
    }

    @Test
    void checkRentalStatus() {
    }
}