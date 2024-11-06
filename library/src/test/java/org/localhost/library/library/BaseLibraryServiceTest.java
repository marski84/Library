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
import org.localhost.library.library.dto.RentalDto;
import org.localhost.library.library.dto.SuccessfulRentalDto;
import org.localhost.library.library.exceptions.RentalException;
import org.localhost.library.library.exceptions.messages.RentalError;
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

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseLibraryServiceTest {


    private final String TEST_USER_NAME = "testName";
    private final String TEST_FIRST_NAME = "firstName";
    private final String TEST_LAST_NAME = "lastName";
    private final int TEST_AGE = 40;
    private final long NON_EXISTING_BOOK_ID = 4255L;
    private final long NON_EXISTING_USER_ID = 4255L;
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

    Book rehisterBook() {
        return bookService.registerBook(bookRegistrationDto);
    }

    RegisteredUserDto testUser() {
        return userService.registerUser(testUserDto);
    }


    @Test
    @DisplayName("rentBookToUser should successfully process rental")
    void rentBookToUser() {
//        given
        Book testBook = rehisterBook();
        RegisteredUserDto testUser = testUser();
//        when
        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(
                testBook.getId(), testUser.getId());
//        then
        assertAll(
                () -> assertEquals(testUser.getId(), testRental.getUserId()),
                () -> assertEquals(testBook.getId(), testBook.getId()),
                () -> assertEquals(testBook.getTitle(), testRental.getBookTitle()),
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
        Book testBook = rehisterBook();
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
        Book testBook = rehisterBook();
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

    @Test
    @DisplayName("getRentalDataForBook should return rental data for book id")
    void getRentalDataForBook() {
//        given
        Book testBook = rehisterBook();
        RegisteredUserDto testUser = testUser();
        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(
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
        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(
                testBook.getId(), testUser.getId());


        bookRegistrationDto = BookRegistrationDto.builder()
                .title("second")
                .author("second")
                .pages(555555)
                .publisher("test publisher")
                .isbn("987654321")
                .build();
        Book secondBook = bookService.registerBook(bookRegistrationDto);

        SuccessfulRentalDto secondTestRental = objectUnderTest.rentBookToUser(secondBook.getId(), testUser.getId());

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
        SuccessfulRentalDto testRental = objectUnderTest.rentBookToUser(
                testBook.getId(), testUser.getId());


        bookRegistrationDto = BookRegistrationDto.builder()
                .title("second")
                .author("second")
                .pages(555555)
                .publisher("test publisher")
                .isbn("987654321")
                .build();
        Book secondBook = bookService.registerBook(bookRegistrationDto);

        SuccessfulRentalDto secondTestRental = objectUnderTest.rentBookToUser(secondBook.getId(), testUser.getId());

        int expectedResultSize = List.of(testRental, secondTestRental).size();

//        when
        List<RentalDto> testResult = objectUnderTest.getRentalDataForUser(testUser.getId());
//        then
        assertEquals(expectedResultSize, testResult.size());
    }
}

