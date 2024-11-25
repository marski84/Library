package org.localhost.library.library.services.QueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.model.Book;
import org.localhost.library.book.repository.BookRepository;
import org.localhost.library.book.service.BookService;
import org.localhost.library.book.service.impl.BaseBookService;
import org.localhost.library.config.service.ConfigService;
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.exceptions.RentalException;
import org.localhost.library.library.exceptions.messages.RentalError;
import org.localhost.library.library.model.Rental;
import org.localhost.library.library.repository.RentalRepository;
import org.localhost.library.library.services.CommandService.impl.BaseRentalCommandService;
import org.localhost.library.library.services.InMemoryConfigService;
import org.localhost.library.library.services.QueryService.impl.BaseRentalQueryService;
import org.localhost.library.library.services.RentalOperationsGateway.RentalOperationsGateway;
import org.localhost.library.library.services.RentalOperationsGateway.impl.BaseRentalOperationsGateway;
import org.localhost.library.repositories.InMemoryBookRepository;
import org.localhost.library.repositories.InMemoryRentalRepository;
import org.localhost.library.repositories.InMemoryUserRepository;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserRegistrationDto;
import org.localhost.library.user.repository.UserRepository;
import org.localhost.library.user.service.UserService;
import org.localhost.library.user.service.impl.BaseUserService;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        configService = new InMemoryConfigService();
        RentalRepository InMemoryRepository = new InMemoryRentalRepository();
        UserRepository inMemoryUserRepository = new InMemoryUserRepository();
        BookRepository inMemoryBookrepository = new InMemoryBookRepository();
        userService = new BaseUserService(inMemoryUserRepository, configService);
        bookService = new BaseBookService(inMemoryBookrepository);

        RentalOperationsGateway rentalOperationsGateway = new BaseRentalOperationsGateway(bookService, userService);
        baseRentalCommandService = new BaseRentalCommandService(InMemoryRepository, rentalOperationsGateway, configService);


        objectUnderTest = new BaseRentalQueryService(InMemoryRepository);

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

    Book registerBook() {
        return bookService.registerBook(bookRegistrationDto);
    }

    RegisteredUserDto testUser() {
        return userService.registerUser(testUserDto);
    }


    @Test
    @DisplayName("getRentalDataForBook should return rental data for book id")
    void getRentalDataForBook() {
//        given
        Book testBook = registerBook();
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
        Book testBook = registerBook();
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
        assertTrue(testResult.isEmpty());
    }

    @Test
    @DisplayName("getActiveRentals should return active rentals list")
    void getActiveRentalsList() {
//        given
        Book testBook = registerBook();
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
    @DisplayName("getOverdueRentals should return empty list when no rentals are overdue")
    void getOverdueRentals() {
//        given
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = baseRentalCommandService.rentBookToUser(
                testBook.getId(), testUser.getId());
        baseRentalCommandService.registerBookReturn(testBook.getId(), testUser.getId(),
                ZonedDateTime.now().plusDays(configService.getRentalPeriodDays() + 1));
//        when
        List<RentalDto> testResult = objectUnderTest.getOverdueRentals();
//        then
        assertTrue(testResult.isEmpty());
    }

    @Test
    @DisplayName("getOverdueRentals should return overdue rentals")
    void getOverdueRentalsList() {
        //        given
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = baseRentalCommandService.rentBookToUser(
                testBook.getId(), testUser.getId());
//        when
        List<RentalDto> testResult = objectUnderTest.getOverdueRentals(
                ZonedDateTime.now().plusDays(configService.getRentalPeriodDays() + 1));
//        then
        assertFalse(testResult.isEmpty());
    }

    @Test
    @DisplayName("getCurrentRentalForBook should return current rental for book")
    void getCurrentRentalForBook() throws InterruptedException {
//        given
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto initialRental = baseRentalCommandService.rentBookToUser(
                testBook.getId(), testUser.getId());

        baseRentalCommandService.registerBookReturn(testBook.getId(), testUser.getId(), ZonedDateTime.now());

        Thread.sleep(100);

        SuccessfulRentalDto secondRental = baseRentalCommandService.rentBookToUser(
                testBook.getId(), testUser.getId());
//        when
        RentalDto testResult = objectUnderTest.getCurrentRentalForBook(testBook.getId());
//        then
        assertAll(
                () -> assertEquals(secondRental.getIsbn(), testResult.getIsbn()),
                () -> assertEquals(secondRental.getAuthor(), testResult.getAuthor()),
                () -> assertEquals(secondRental.getUserId(), testResult.getUserId()),
                () -> assertEquals(secondRental.getRentalTime(), testResult.getRentalTime()),
                () -> assertNotEquals(initialRental.getRentalDate(), testResult.getRentalDate())
        );
    }

    @Test
    @DisplayName("getCurrentRentalForBook should throw when no rental registered")
    void getCurrentRentalForBookShouldReturnNull() {
//        given
        Book testBook = registerBook();
//        when
        Exception e = assertThrows(
                RentalException.class,
                () -> objectUnderTest.getCurrentRentalForBook(testBook.getId())
        );
//        then
        assertEquals(RentalError.RENTAL_NOT_FOUND.getMessage(), e.getMessage());
    }

    @Test
    @DisplayName("isBookAvailableForRental should return true when book is available")
    void isBookAvailableForRental() throws InterruptedException {
        //        given
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = baseRentalCommandService.rentBookToUser(
                testBook.getId(), testUser.getId());

        Rental testResult = baseRentalCommandService.registerBookReturn(testBook.getId(), testUser.getId(), ZonedDateTime.now());
        //        when
        boolean isAvailable = objectUnderTest.isBookAvailableForRental(testResult.getBook().getId());
//        then
        assertTrue(isAvailable);
    }

    @Test
    @DisplayName("isBookAvailableForRental should return false when book is not available")
    void isBookAvailableForRentalShouldReturnFalse() {
        //        given
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
        baseRentalCommandService.rentBookToUser(testBook.getId(), testUser.getId());
//        when
        boolean testResult = objectUnderTest.isBookAvailableForRental(testBook.getId());
//        then
        assertFalse(testResult);
    }

    @Test
    @DisplayName("isBookAvailableForRental should throw when no book is registered")
    void isBookAvailableForRentalShouldThrow() {
//        when
        Exception testResult = assertThrows(
                RentalException.class,
                () -> objectUnderTest.isBookAvailableForRental(NON_EXISTING_BOOK_ID)
        );
//        then
                assertEquals(testResult.getMessage(), RentalError.RENTAL_NOT_FOUND.getMessage());
//                assertEquals(testResult.getMessage(), RentalError.RENTAL_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("getNumberOfActiveRentalsForUser should return number of active rentals for user")
    void getNumberOfActiveRentalsForUser() {
//        given
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
        baseRentalCommandService.rentBookToUser(testBook.getId(), testUser.getId());
        bookRegistrationDto = BookRegistrationDto.builder()
                .title("second")
                .author("second")
                .pages(555555)
                .publisher("test publisher")
                .isbn("987654321")
                .build();
        Book secondBook = bookService.registerBook(bookRegistrationDto);
        baseRentalCommandService.rentBookToUser(secondBook.getId(), testUser.getId());
        int amountOfRentals = objectUnderTest.getActiveRentals().size();
//        when
        int testResult = objectUnderTest.getNumberOfActiveRentalsForUser(testUser.getId());
//        then
        assertEquals(amountOfRentals, testResult);
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