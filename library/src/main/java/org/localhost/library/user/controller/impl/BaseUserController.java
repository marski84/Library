package org.localhost.library.user.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.localhost.library.library.RentalStatus;
import org.localhost.library.user.controller.UserController;
import org.localhost.library.user.dto.EditUserDataDto;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.dto.UserRegistrationDto;
import org.localhost.library.user.model.User;
import org.localhost.library.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class BaseUserController implements UserController {
    private final UserService userService;

    public BaseUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisteredUserDto> registerUser(@Valid UserRegistrationDto userRegistrationDto) {
        RegisteredUserDto user = userService.registerUser(userRegistrationDto);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@Positive long userId, @Valid EditUserDataDto userDto) {
        UserDto user = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserStatus(@Positive @PathVariable long userId) {
        UserDto user = userService.getUserStatus(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/findUser/{id}")
    public ResponseEntity<User> findUserById(@PathVariable @Positive long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/block/{id}")
    public ResponseEntity<?> blockUser(@PathVariable @Positive long id) {
        userService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unblock/{id}")
    public ResponseEntity<?> unblockUser(@PathVariable @Positive long id) {
        userService.unblockUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/updatePenaltyPoints/{userId}/{maxPenaltyPoints}/{rentalStatus}")
    public ResponseEntity<?> updateUserPenaltyPoints(
            @PathVariable @Positive long userId,
            @PathVariable @Positive int maxPenaltyPoints,
            @PathVariable RentalStatus rentalStatus) {
        userService.updateUserPenaltyPoints(userId, maxPenaltyPoints, rentalStatus);
        return ResponseEntity.ok().build();
    }
}
