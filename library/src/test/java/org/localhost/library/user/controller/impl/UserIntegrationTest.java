package org.localhost.library.user.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.localhost.library.book.service.BookService;
import org.localhost.library.library.RentalStatus;
import org.localhost.library.user.dto.EditUserDataDto;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.dto.UserRegistrationDto;
import org.localhost.library.user.model.User;
import org.localhost.library.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    private static final String USERS_URL = "/api/users";
    private static final String USERS_GET_ALL_URL = USERS_URL + "/all";
    private static final int NOT_EXISTING_USER_ID = 999;
    private static final int TEST_USER_ID = 1;


    @Test
    void registerUserShouldReturn200WhenRegisterDtoIsValid() throws Exception {
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                .userName("testUser")
                .age(25)
                .firstName("Test")
                .lastName("User")
                .build();

        mockMvc.perform(post(USERS_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(userRegistrationDto.getUserName()))
                .andExpect(jsonPath("$.age").value(userRegistrationDto.getAge()))
                .andExpect(jsonPath("$.firstName").value(userRegistrationDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userRegistrationDto.getLastName()));
    }

    @Test
    void registerUserWithExistingUserNameShouldReturn400WhenRegistrationDtoNotValid() throws Exception {
        final String errorMessage = "User already exists!";
        final int errorCode = 2000;
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                .userName("testUser")
                .age(25)
                .firstName("Test")
                .lastName("User")
                .build();

        mockMvc.perform(post(USERS_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(errorMessage))
                        .andExpect(jsonPath("$.errorCode").value(errorCode));
    }


    @Test
    void updateUserShouldReturn200WhenEditUserDtoValid() throws Exception{
//        given
         EditUserDataDto updatedTestUser = EditUserDataDto.builder()
                .age(25)
                .firstName("Test")
                .lastName("User")
                 .build();

//        when & then
        mockMvc.perform(put(USERS_URL + "/update/{userId}", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTestUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(updatedTestUser.getAge()))
                .andExpect(jsonPath("$.firstName").value(updatedTestUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedTestUser.getLastName()))
                .andExpect(jsonPath("$.id").value(TEST_USER_ID));
    }


    @Test
    void updateUserShouldReturn400WhenEditUserDtoIsNotValid() throws Exception {
        final String errorMessage = "Edit dto validation failure";
        final int errorCode = 600;
//        given
        EditUserDataDto updatedTestUser = EditUserDataDto.builder()
                .age(0)
                .firstName("")
                .lastName("")
                .build();

//        when & then
        mockMvc.perform(put(USERS_URL + "/update/{userId}", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTestUser)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(errorMessage))
                        .andExpect(jsonPath("$.errorCode").value(errorCode));

    }


    @Test
    void getUserStatus() throws Exception {
        User testUserData = userService.findUserById(TEST_USER_ID);
//        when & then
        mockMvc.perform(get(USERS_URL +"/{userId}", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(testUserData.getUserName()))
                .andExpect(jsonPath("$.age").value(testUserData.getAge()))
                .andExpect(jsonPath("$.firstName").value(testUserData.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testUserData.getLastName()))
                .andExpect(jsonPath("$.penaltyPoints").value(testUserData.getPenaltyPoints()))
                .andExpect(jsonPath("$.blocked").value(testUserData.isBlocked()));
    }

    @Test
    void getAllUsers() throws Exception {
//        given
        User expectedResult = userService.findUserById(TEST_USER_ID);
        int expectedLength = userService.getAllUsers().size();
//        when & then
        mockMvc.perform(get(USERS_GET_ALL_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedLength))
                .andExpect(jsonPath("$[0].userName").value(expectedResult.getUserName()))
                .andExpect(jsonPath("$[0].age").value(expectedResult.getAge()))
                .andExpect(jsonPath("$[0].penaltyPoints").value(expectedResult.getPenaltyPoints()))
                .andExpect(jsonPath("$[0].firstName").value(expectedResult.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(expectedResult.getLastName()))
                .andExpect(jsonPath("$[0].blocked").value(expectedResult.isBlocked()));
    }

    @Test
    void blockUserShouldReturn200() throws Exception {
//        given
        User testUserData = userService.findUserById(TEST_USER_ID);
//        when
        mockMvc.perform(get(USERS_URL + "/block/{userId}", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
//        then
        mockMvc.perform(get(USERS_URL +"/{userId}", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(testUserData.getUserName()))
                .andExpect(jsonPath("$.age").value(testUserData.getAge()))
                .andExpect(jsonPath("$.firstName").value(testUserData.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testUserData.getLastName()))
                .andExpect(jsonPath("$.penaltyPoints").value(testUserData.getPenaltyPoints()))
                .andExpect(jsonPath("$.blocked").value(true));
    }

    @Test
    void blockUserShouldReturn400whenUserDoesNotExist() throws Exception {
        final String errorMessage = "User not found";
        final int errorCode = 2100;
//        when & then
        mockMvc.perform(get(USERS_URL + "/block/{userId}", NOT_EXISTING_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(errorMessage))
                        .andExpect(jsonPath("$.errorCode").value(errorCode));
    }

    @Test
    void unblockUserShouldReturn200() throws Exception {
//        given
        mockMvc.perform(get(USERS_URL + "/block/{userId}", TEST_USER_ID))
                        .andExpect(status().isOk());
//        when
        mockMvc.perform(get(USERS_URL + "/unblock/{userId}", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
//        then
                mockMvc.perform(get(USERS_URL +"/{userId}", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.blocked").value(false));
    }

    @Test
    void updateUserPenaltyPointsShouldReturn200() throws Exception {
//        when
        mockMvc.perform(get(USERS_URL + "/updatePenaltyPoints/{userId}/{rentalStatus}",
                TEST_USER_ID, RentalStatus.OVERDUE))
                .andExpect(status().isOk());
    //        then}
        mockMvc.perform(get(USERS_URL +"/{userId}", TEST_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blocked").value(true));

    }

    @Test
    void updateUserPenaltyPointsShouldReturn200AndBlockUser() throws Exception {
//        given
        int penaltyPoints = 10;
//        when
        mockMvc.perform(get(USERS_URL + "/updatePenaltyPoints/{userId}/{rentalStatus}",
                        TEST_USER_ID, RentalStatus.OVERDUE))
                .andExpect(status().isOk());
        Thread.sleep(500);
        mockMvc.perform(get(USERS_URL + "/updatePenaltyPoints/{userId}/{rentalStatus}",
                        TEST_USER_ID, RentalStatus.OVERDUE))
                .andExpect(status().isOk());
//        then
        mockMvc.perform(get(USERS_URL +"/{userId}", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blocked").value(true));
    }
}