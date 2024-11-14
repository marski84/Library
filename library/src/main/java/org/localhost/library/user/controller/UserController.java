package org.localhost.library.user.controller;

import org.localhost.library.library.RentalStatus;
import org.localhost.library.user.dto.EditUserDataDto;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.dto.UserRegistrationDto;
import org.localhost.library.user.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserController {
    ResponseEntity<RegisteredUserDto> registerUser(UserRegistrationDto userRegistrationDto);
    ResponseEntity<UserDto> updateUser(long userId, EditUserDataDto userDto);
    ResponseEntity<UserDto> getUserStatus(long userId);
    ResponseEntity<List<UserDto>> getAllUsers();
    ResponseEntity<User> findUserById(long id);
    ResponseEntity<?> blockUser(long id);
    ResponseEntity<?> unblockUser(long id);
    ResponseEntity<?> updateUserPenaltyPoints(long userId, int maxPenaltyPoints, RentalStatus rentalStatus);
}
