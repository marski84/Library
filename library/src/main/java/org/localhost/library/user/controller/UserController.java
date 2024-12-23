package org.localhost.library.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.localhost.library.library.RentalStatus;
import org.localhost.library.user.dto.EditUserDataDto;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.dto.UserRegistrationDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserController {
    ResponseEntity<RegisteredUserDto> registerUser(@Valid UserRegistrationDto userRegistrationDto);
    ResponseEntity<UserDto> updateUser(@Positive @NotNull Long userId, @Valid EditUserDataDto userDto);
    ResponseEntity<UserDto> getUserStatus(@Positive @NotNull Long userId);
    ResponseEntity<List<UserDto>> getAllUsers();
    ResponseEntity<?> blockUser(@Positive @NotNull Long id);
    ResponseEntity<?> unblockUser(@Positive @NotNull Long id);
    ResponseEntity<?> updateUserPenaltyPoints(@Positive @NotNull Long userId, RentalStatus rentalStatus);
}
