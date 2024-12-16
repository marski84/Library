package org.localhost.library.library.services.CommandService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.model.Book;
import org.localhost.library.book.repository.BookRepository;
import org.localhost.library.book.service.BookService;
import org.localhost.library.book.service.impl.BaseBookService;
import org.localhost.library.config.service.ConfigService;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.exceptions.RentalException;
import org.localhost.library.library.exceptions.messages.RentalError;
import org.localhost.library.library.model.Rental;
import org.localhost.library.library.repository.RentalRepository;
import org.localhost.library.library.services.CommandService.impl.BaseRentalCommandService;
import org.localhost.library.library.services.InMemoryConfigService;
import org.localhost.library.library.services.RentalOperationsGateway.RentalOperationsGateway;
import org.localhost.library.library.services.RentalOperationsGateway.impl.BaseRentalOperationsGateway;
import org.localhost.library.repositories.InMemoryBookRepository;
import org.localhost.library.repositories.InMemoryRentalRepository;
import org.localhost.library.repositories.InMemoryUserRepository;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserRegistrationDto;
import org.localhost.library.user.model.User;
import org.localhost.library.user.repository.UserRepository;
import org.localhost.library.user.service.UserService;
import org.localhost.library.user.service.impl.BaseUserService;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BaseRentalCommandServiceTest {
    private final String TEST_USER_NAME = "testName";
    private final String TEST_FIRST_NAME = "firstName";
    private final String TEST_LAST_NAME = "lastName";
    private final int TEST_AGE = 40;
    private final long NON_EXISTING_BOOK_ID = 4255L;
    private final long NON_EXISTING_USER_ID = 4255L;
    private final String EDITED_USER_NAME = "editedName";
    private int testRentalTime;

    private RentalCommandService objectUnderTest;
    private UserRegistrationDto testUserDto;
    private BookRegistrationDto bookRegistrationDto;
    private RentalOperationsGateway rentalOperationsGateway;

    private UserService userService;
    private BookService bookService;
    private ConfigService configService;

    @BeforeEach
    void setUp() {
        RentalRepository InMemoryRepository = new InMemoryRentalRepository();
        UserRepository inMemoryUserRepository = new InMemoryUserRepository();
        BookRepository inMemoryBookrepository = new InMemoryBookRepository();
        configService = new InMemoryConfigService();


        userService = new BaseUserService(inMemoryUserRepository, configService);
        bookService = new BaseBookService(inMemoryBookrepository);
        rentalOperationsGateway = new BaseRentalOperationsGateway(bookService, userService);

        objectUnderTest = new BaseRentalCommandService(InMemoryRepository, rentalOperationsGateway, configService);

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
    @DisplayName("rentBookToUser should successfully process rental")
    void rentBookToUser() {
//        given
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
//        when
        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(
                testBook.getId(), testUser.getId());
//        then
        assertAll(
                () -> assertEquals(testUser.getId(), testRental.getUserId()),
                () -> assertEquals(testBook.getTitle(), testRental.getBookTitle()),
                () -> assertEquals(testBook.getIsbn(), testRental.getIsbn()),
                () -> assertEquals(testBook.getAuthor(), testRental.getAuthor()),
                () -> assertEquals(testRentalTime, testRental.getRentalTime())
        );
    }

    @Test
    @DisplayName("rentBookToUser should throw when book is already rented")
    void rentBookToUserWhenBookIsAlreadyRented() {
//        given
        RegisteredUserDto testUser = userService.registerUser(testUserDto);
        Book testBook = bookService.registerBook(bookRegistrationDto);
        objectUnderTest.rentBookToUser(testBook.getId(), testUser.getId());
//        when, then
        RentalException rentalException = assertThrows(
                RentalException.class,
                () -> objectUnderTest.rentBookToUser(testBook.getId(), testUser.getId())
        );
        assertEquals(RentalError.RENTAL_NOT_POSSIBLE.getCode(), rentalException.getErrorCode().getCode());
    }

    @Test
    @DisplayName("rentBookToUser should throw user is blocked")
    void rentBookToUserWhenUserIsBlocked() {
//        given
        RegisteredUserDto testUser = userService.registerUser(testUserDto);
        userService.blockUser(testUser.getId());
        Book book = bookService.registerBook(bookRegistrationDto);
//        when, then
        RentalException rentalException = assertThrows(
                RentalException.class,
                () -> objectUnderTest.rentBookToUser(book.getId(), testUser.getId())
        );
        assertEquals(RentalError.RENTAL_NOT_POSSIBLE.getCode(), rentalException.getErrorCode().getCode());
    }


    @Test
    @DisplayName("registerBookReturn should successfully register return od the book")
    void registerBookReturn() {
//        given
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto rental = objectUnderTest.rentBookToUser(testBook.getId(), testUser.getId());
        System.out.println(rental);
        ZonedDateTime returnData = ZonedDateTime.now();
//        when
        Rental bookReturnConfirmation = objectUnderTest.registerBookReturn(
                testBook.getId(),
                testUser.getId(),
                returnData
        );
//        then
        assertAll(
                () -> assertEquals(bookReturnConfirmation.getReturnDate(), returnData)
        );
    }

    @Test
    @DisplayName("registerBookReturn should throw when no book rental is found")
    void registerBookReturnWhenNoBookRentalFound() {
        RentalException rentalException = assertThrows(
                RentalException.class,
                () -> objectUnderTest.registerBookReturn(
                        NON_EXISTING_BOOK_ID,
                        NON_EXISTING_USER_ID,
                        ZonedDateTime.now()
                )
        );
        assertEquals(RentalError.RENTAL_NOT_FOUND.getCode(), rentalException.getErrorCode().getCode());

    }

    @Test
    @DisplayName("registerBookReturn should throw when return date is earlier than rent date")
    void registerBookReturnWhenReturnDateIsEarlierThanRentDate() {
        //        given
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(
                testBook.getId(), testUser.getId());

        ZonedDateTime sevenDaysEarlier = (ZonedDateTime.now()).minusDays(7);
//        when, then
        RentalException rentalException = assertThrows(
                RentalException.class
                , () -> objectUnderTest.registerBookReturn(
                        testBook.getId(),
                        testUser.getId(),
                        sevenDaysEarlier
                )
        );
        assertEquals(RentalError.NOT_VALID_RETURN_DATE.getCode(), rentalException.getErrorCode().getCode());

    }

    @DisplayName("calculatePenaltyPoints should add penalty points to user")
    @Test
    void calculatePenaltyPoints() {
//        give
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(
                testBook.getId(), testUser.getId());
//        when
        ZonedDateTime overdueDate = testRental.getDueDate().plusDays(configService.getRentalPeriodDays());
        System.out.println(overdueDate);
        objectUnderTest.registerBookReturn(testBook.getId(), testUser.getId(), overdueDate);
//        then
        User testResult = userService.findUserById(testUser.getId());
        assertEquals(configService.getLateOverduePoints(), testResult.getPenaltyPoints());
    }

    @DisplayName("extendRental should extend rental period")
    @Test
    void extendRentalPeriod() {
//        given
        Book testBook = registerBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(
                testBook.getId(), testUser.getId());
        int AMOUNT_OF_DAYS_TO_EXTEND = 5;

//        when
       ZonedDateTime extendedDueDate =  objectUnderTest.extendRental(testRental.getRentalId(), AMOUNT_OF_DAYS_TO_EXTEND);
//        then
    assertEquals(testRental.getDueDate().plusDays(AMOUNT_OF_DAYS_TO_EXTEND), extendedDueDate);


    }

//    @DisplayName("extendRental should extend due date")
//    @Test
//    void extendRentalDate(){
//// given
//        Book testBook = registerBook();
//        RegisteredUserDto testUser = testUser();
//        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(testUser.getId(), testBook.getId());
//        int extendedRentalPeriodDays = 10;
////        when
//        ZonedDateTime testResult = objectUnderTest.extendRental(testRental.getRentalId(), extendedRentalPeriodDays);
//        assertEquals(testRental.getDueDate().plusDays(extendedRentalPeriodDays), testResult);
//    }
}