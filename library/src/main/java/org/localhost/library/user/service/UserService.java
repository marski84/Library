package org.localhost.library.user.service;

import org.localhost.library.library.RentalStatus;
import org.localhost.library.user.dto.EditUserDataDto;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.dto.UserRegistrationDto;
import org.localhost.library.user.model.User;

import java.util.List;

public interface UserService {
    RegisteredUserDto registerUser(UserRegistrationDto userRegistrationDto);

    UserDto removeUser(long id);

    UserDto updateUser(long userId, EditUserDataDto userDto);

    UserDto getUserStatus(long userId);

    List<UserDto> getAllUsers();

    User findUserById(long id);

    void blockUser(long id);

    void unblockUser(long id);

    void updateUserPenaltyPoints(long userId, int maxPenaltyPoints, RentalStatus rentalStatus);
}
