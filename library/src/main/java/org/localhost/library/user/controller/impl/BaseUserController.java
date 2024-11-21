package org.localhost.library.user.controller.impl;

import org.localhost.library.library.RentalStatus;
import org.localhost.library.user.controller.UserController;
import org.localhost.library.user.dto.EditUserDataDto;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.dto.UserRegistrationDto;
import org.localhost.library.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<RegisteredUserDto> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        RegisteredUserDto user = userService.registerUser(userRegistrationDto);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long userId,
            @RequestBody @Validated EditUserDataDto userDto) {
        UserDto user = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserStatus(@PathVariable Long userId) {
        UserDto user = userService.getUserStatus(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/block/{id}")
    public ResponseEntity<?> blockUser(@PathVariable Long id) {
        userService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unblock/{id}")
    public ResponseEntity<?> unblockUser(@PathVariable Long id) {
        userService.unblockUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/updatePenaltyPoints/{userId}/{rentalStatus}")
    public ResponseEntity<?> updateUserPenaltyPoints(
            @PathVariable Long userId,
            @PathVariable RentalStatus rentalStatus) {
        userService.updateUserPenaltyPoints(userId, rentalStatus);
        return ResponseEntity.ok().build();
    }
}
