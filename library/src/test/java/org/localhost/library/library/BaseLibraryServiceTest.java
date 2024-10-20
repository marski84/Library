package org.localhost.library.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.localhost.library.book.BaseBookService;
import org.localhost.library.book.BookRepository;
import org.localhost.library.book.BookService;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.model.Book;
import org.localhost.library.config.ConfigService;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.exceptions.NotValidReturnDateException;
import org.localhost.library.library.exceptions.RentalNotFoundException;
import org.localhost.library.library.exceptions.RentalNotPossibleException;
import org.localhost.library.library.model.Rental;
import org.localhost.library.library.services.InMemoryConfigService;
import org.localhost.library.repositories.InMemoryBookRepository;
import org.localhost.library.repositories.InMemoryRentalRepository;
import org.localhost.library.repositories.InMemoryUserRepository;
import org.localhost.library.user.BaseUserService;
import org.localhost.library.user.UserRepository;
import org.localhost.library.user.UserService;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserRegistrationDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class BaseLibraryServiceTest {


    private final String TEST_USER_NAME = "testName";
    private final String TEST_FIRST_NAME = "firstName";
    private final String TEST_LAST_NAME = "lastName";
    private final int TEST_AGE = 40;
    private final long NON_EXISTING_BOOK_ID = 4255l;
    private final long NON_EXISTING_USER_ID = 4255l;
    private final String EDITED_USER_NAME = "editedName";
    private int testRentalTime;

    private LibraryService objectUnderTest;
    private UserRegistrationDto testUserDto;
    private BookRegistrationDto bookRegistrationDto;

    private UserService userService;
    private BookService bookService;
    private ConfigService configService;

    @BeforeEach
    void setUp() {
        RentalRepository InMemoryRepository = new InMemoryRentalRepository();
        UserRepository inMemoryUserRepository = new InMemoryUserRepository();
        BookRepository inMemoryBookrepository = new InMemoryBookRepository();

        userService = new BaseUserService(inMemoryUserRepository);
        bookService = new BaseBookService(inMemoryBookrepository);
        configService = new InMemoryConfigService();

        objectUnderTest = new BaseLibraryService(InMemoryRepository, bookService, userService, configService);

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

    Book rentABook() {
        return bookService.registerBook(bookRegistrationDto);
    }

    RegisteredUserDto testUser() {
        return userService.registerUser(testUserDto);
    }


    @Test
    @DisplayName("rentBookToUser should successfully process rental")
    void rentBookToUser() {
//        given
        Book testBook = rentABook();
        RegisteredUserDto testUser = testUser();
//        when
        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(
                testBook.getId(), testUser.getId());
//        then
    assertAll(
            ()-> assertEquals(testUser.getId(), testRental.getUserId()),
            ()-> assertEquals(testBook.getId(), testBook.getId()),
            ()-> assertEquals(testBook.getTitle(), testRental.getBookTitle()),
            ()-> assertEquals(testBook.getAuthor(), testRental.getAuthor()),
            ()-> assertEquals(testRentalTime, testRental.getRentalTime())
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
        assertThrows(
                RentalNotPossibleException.class,
                () -> objectUnderTest.rentBookToUser(testBook.getId(), testUser.getId())
        );
    }

    @Test
    @DisplayName("rentBookToUser should throw user is blocked")
    void rentBookToUserWhenUserIsBlocked() {
//        given
        RegisteredUserDto testUser = userService.registerUser(testUserDto);
        userService.blockUser(testUser.getId());
        Book book = bookService.registerBook(bookRegistrationDto);
//        when, then
        assertThrows(
                RentalNotPossibleException.class,
                () -> objectUnderTest.rentBookToUser(book.getId(), testUser.getId())
        );
    }


    @Test
    @DisplayName("registerBookReturn should successfully register return od the book")
    void registerBookReturn() {
//        given
        Book testBook = rentABook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto rental = objectUnderTest.rentBookToUser(testBook.getId(), testUser.getId());
        System.out.println(rental);
        Instant returnData = Instant.now();
//        when
        Rental bookReturnConfirmation = objectUnderTest.registerBookReturn(
                testBook.getId(),
                testUser.getId(),
                returnData
        );
//        then
        assertAll(
                ()-> assertEquals(bookReturnConfirmation.getReturnDate(), returnData)
        );
    }

    @Test
    @DisplayName("registerBookReturn should throw when no book rental is found")
    void registerBookReturnWhenNoBookRentalFound() {
        assertThrows(
                RentalNotFoundException.class,
                () -> objectUnderTest.registerBookReturn(
                        NON_EXISTING_BOOK_ID,
                        NON_EXISTING_USER_ID,
                        Instant.now()
                )
        );
    }

    @Test
    @DisplayName("registerBookReturn should throw when return date is earlier than rent date")
    void registerBookReturnWhenReturnDateIsEarlierThanRentDate() {
        //        given
        Book testBook = rentABook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(
                testBook.getId(), testUser.getId());

        Instant sevenDaysEarlier = (Instant.now()).minus(7, ChronoUnit.DAYS);
//        when, then
        assertThrows(
                NotValidReturnDateException.class
                , () -> objectUnderTest.registerBookReturn(
                        testBook.getId(),
                        testUser.getId(),
                        sevenDaysEarlier
                )
        );
    }
}

