package org.localhost.library.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.localhost.library.repositories.InMemoryUserRepository;
import org.localhost.library.user.dto.EditUserDataDto;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.dto.UserRegistrationDto;
import org.localhost.library.user.exceptions.UserAlreadyExistsException;
import org.localhost.library.user.exceptions.UserNotFoundException;
import org.localhost.library.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseUserServiceTest {

    private UserService objectUnderTest;
    private UserRegistrationDto testUserDto;
    private final String TEST_USER_NAME = "testName";
    private final String TEST_FIRST_NAME = "firstName";
    private final String TEST_LAST_NAME = "lastName";
    private final int TEST_AGE = 40;
    private final Long NON_EXISTING_USER_ID = 4255L;
    private final String EDITED_USER_NAME = "editedName";


    @BeforeEach
    void setUp() {
        UserRepository userRepository = new InMemoryUserRepository();
        objectUnderTest = new BaseUserService(userRepository);
        testUserDto = UserRegistrationDto.builder()
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .age(TEST_AGE)
                .build();
    }


    @Test
    @DisplayName("it should register a new User")
    void registerUser() {
//        given, when
        RegisteredUserDto testResult = objectUnderTest.registerUser(testUserDto);
//        then
        assertAll(
                () -> assertNotNull(testResult),
                () -> assertEquals(TEST_USER_NAME, testResult.getUserName()),
                () -> assertEquals(TEST_FIRST_NAME, testResult.getFirstName()),
                () -> assertEquals(TEST_LAST_NAME, testResult.getLastName()),
                () -> assertEquals(TEST_AGE, testResult.getAge())
        );

    }


    @Test
    @DisplayName("it should throw IllegalArgumentException when userRegistrationDto is null")
    void registerUserWithNullDto() {
        assertThrows(IllegalArgumentException.class, () -> objectUnderTest.registerUser(null));
    }

    @Test
    @DisplayName("it should throw exception when registering user with existing username")
    void registerUserWithExistingUsername() {
        // given
        objectUnderTest.registerUser(testUserDto);
        // when & then
        assertThrows(
                UserAlreadyExistsException.class,
                () -> objectUnderTest.registerUser(testUserDto));
    }

    @Test
    @DisplayName("removeUser should successfully remove user")
    void removeUser() {
//        given
        RegisteredUserDto testUser = objectUnderTest.registerUser(testUserDto);
//        when
        UserDto testResult = objectUnderTest.removeUser(testUser.getId());
//        then
        assertNotNull(testResult);
        assertEquals(testUserDto.getUserName(), testResult.getUserName());
    }

    @Test
    @DisplayName("removeUser should throw when no user found")
    void removeUserNoUser() {
        assertThrows(
                UserNotFoundException.class,
                () -> objectUnderTest.removeUser(NON_EXISTING_USER_ID)
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    @DisplayName("removeUser should throw IllegalArgumentException when userId is <= 0")
    void removeUserWithNullDto(long invalidId) {
        assertThrows(
                IllegalArgumentException.class,
                () -> objectUnderTest.removeUser(invalidId));
    }

    @Test
    @DisplayName("updateUser should successfully update user data")
    void updateUser() {
//        given
        RegisteredUserDto testUser = objectUnderTest.registerUser(testUserDto);
        EditUserDataDto editUserDataDto = EditUserDataDto.builder()
                .firstName(EDITED_USER_NAME)
                .lastName(EDITED_USER_NAME)
                .age(TEST_AGE + 1)
                .build();
//        when
        UserDto testResult = objectUnderTest.updateUser(testUser.getId(), editUserDataDto);
//        then
        assertAll(
                () -> assertNotNull(testResult),
                () -> assertEquals(testUserDto.getUserName(), testResult.getUserName()),
                () -> assertEquals(EDITED_USER_NAME, testResult.getFirstName()),
                () -> assertEquals(EDITED_USER_NAME, testResult.getLastName()),
                () -> assertEquals(TEST_AGE + 1, testResult.getAge())
        );

    }

    @Test
    @DisplayName("updateUser should throw when update data is null")
    void updateUserWithNullDto() {
//        given
        RegisteredUserDto activeUser = objectUnderTest.registerUser(testUserDto);
//        when, then
        assertThrows(
                IllegalArgumentException.class,
                () -> objectUnderTest.updateUser(activeUser.getId(), null)
        );
    }

    @Test
    @DisplayName("updateUser should throw when user not existing")
    void updateUserNoUser() {
//        given
        EditUserDataDto editData = EditUserDataDto.builder().build();
        RegisteredUserDto activeUser = objectUnderTest.registerUser(testUserDto);
//        when, then
        assertThrows(
                UserNotFoundException.class,
                () -> objectUnderTest.updateUser(NON_EXISTING_USER_ID, editData)
        );
    }

    @Test
    @DisplayName("getUserStatus should successfully return user data")
    void getUserStatus() {
//        given
        RegisteredUserDto activeUser = objectUnderTest.registerUser(testUserDto);
//        when
        UserDto testResult = objectUnderTest.getUserStatus(activeUser.getId());
//        then
        assertAll(
                () -> assertEquals(activeUser.getId(), testResult.getId()),
                () -> assertEquals(activeUser.getUserName(), testResult.getUserName()),
                () -> assertEquals(activeUser.getPenaltyPoints(), testResult.getPenaltyPoints()),
                () -> assertFalse(testResult.isBlocked())
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    @DisplayName("getUserData should throw IllegalArgumentException when id <= 0")
    void getUserDataWithInvalidId(long invalidId) {
        assertThrows(IllegalArgumentException.class, () -> objectUnderTest.getUserStatus(invalidId));
    }


    @Test
    @DisplayName("findUserById should find and return userData")
    void findUserById() throws UserNotFoundException {
//        given
        RegisteredUserDto testUser = objectUnderTest.registerUser(testUserDto);
//        when
        User testResult = objectUnderTest.findUserById(testUser.getId());
//        then
        assertAll(
                () -> assertEquals(
                        testUser.getId(),
                        testResult.getId()
                ),
                () -> assertEquals(
                        testUser.getUserName(),
                        testResult.getUserName()
                ),
                () -> assertEquals(
                        testUser.getFirstName(),
                        testResult.getFirstName()
                )
        );
    }


    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    @DisplayName("findUserById should throw IllegalArgumentException when id <= 0")
    void findUserById(long invalidId) {
        assertThrows(
                IllegalArgumentException.class,
                () -> objectUnderTest.findUserById(invalidId)
        );
    }

    @Test
    @DisplayName("getAllUsers should return a user list")
    void getAllUsers() {
//        given
        RegisteredUserDto firstUser = objectUnderTest.registerUser(testUserDto);
        UserRegistrationDto secondUserData = UserRegistrationDto.builder()
                .userName("secondUser")
                .firstName("secondUser first name")
                .lastName("secondUser last name")
                .build();
        RegisteredUserDto secondUser = objectUnderTest.registerUser(secondUserData);
        //        when
        List<UserDto> testResultList = objectUnderTest.getAllUsers();
        //        then
        assertAll(
                ()-> assertEquals(2, testResultList.size()),
                ()-> assertEquals(firstUser.getId(), testResultList.get(0).getId()),
                ()-> assertEquals(secondUser.getId(), testResultList.get(1).getId())
        );
    }

    @Test
    @DisplayName("blockUser should successfully block user")
    void blockUser() {
//        given
        RegisteredUserDto testUser = objectUnderTest.registerUser(testUserDto);
//        when
        objectUnderTest.blockUser(testUser.getId());
        User testResult = objectUnderTest.findUserById(testUser.getId());
//        then
        assertTrue(testResult.isBlocked());
    }

    @Test
    @DisplayName("unblockUser should activate blocked user")
    void unblockUser() {
//        given
        RegisteredUserDto testUser = objectUnderTest.registerUser(testUserDto);
        objectUnderTest.blockUser(testUser.getId());
//        when
        objectUnderTest.unblockUser(testUser.getId());
        User testResult = objectUnderTest.findUserById(testUser.getId());
//        then
        assertFalse(testResult.isBlocked());
    }
}