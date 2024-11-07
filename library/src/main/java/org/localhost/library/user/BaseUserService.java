package org.localhost.library.user;

import org.localhost.library.config.ConfigService;
import org.localhost.library.library.RentalStatus;
import org.localhost.library.user.dto.EditUserDataDto;
import org.localhost.library.user.dto.RegisteredUserDto;
import org.localhost.library.user.dto.UserDto;
import org.localhost.library.user.dto.UserRegistrationDto;
import org.localhost.library.user.exceptions.UserException;
import org.localhost.library.user.exceptions.messages.UserError;
import org.localhost.library.user.model.User;
import org.localhost.library.utils.AppLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class BaseUserService implements UserService {

    private final UserRepository userRepository;
    private final ConfigService configService;

    public BaseUserService(UserRepository userRepository, ConfigService configService) {
        this.userRepository = userRepository;
        this.configService = configService;
    }

    @Override
    @Transactional
    public RegisteredUserDto registerUser(UserRegistrationDto userRegistrationDto) {
        if (userRegistrationDto == null) {
            AppLogger.logError("UserRegistrationDto is null");
            throw new IllegalArgumentException("userRegistrationDto cannot be null");
        }

        if (userRepository.existsByUserName(userRegistrationDto.getUserName())) {
            UserException userException = new UserException(UserError.USER_EXISTS);
            AppLogger.logError(userException.getError().getCode() + "for username: " + userRegistrationDto.getUserName());
            throw userException;
        }

        User newUser = new User();
        newUser.setUserName(userRegistrationDto.getUserName());
        newUser.setAge(userRegistrationDto.getAge());
        newUser.setFirstName(userRegistrationDto.getFirstName());
        newUser.setLastName(userRegistrationDto.getLastName());

        User registeredUser = userRepository.save(newUser);
        AppLogger.logInfo("Registered user: " + registeredUser);

        return RegisteredUserDto.builder()
                .id(registeredUser.getId())
                .userName(registeredUser.getUserName())
                .firstName(registeredUser.getFirstName())
                .lastName(registeredUser.getLastName())
                .age(registeredUser.getAge())
                .penaltyPoints(registeredUser.getPenaltyPoints())
                .isBlocked(registeredUser.isBlocked())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto removeUser(long userId) {
        validateUserId(userId);
        User userToRemove = findUserById(userId);

        userRepository.delete(userToRemove);

        AppLogger.logInfo("Removed user with id: " + userToRemove.getId());

        return UserDto.builder()
                .id(userToRemove.getId())
                .userName(userToRemove.getUserName())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto updateUser(long userId, EditUserDataDto userDto) {
        if (userDto == null) {
            AppLogger.logInfo("UserDto is null for " + userId);
            throw new IllegalArgumentException("userDto cannot be null");
        }
        validateUserId(userId);

        User userToEdit = findUserById(userId);

        if (userDto.getFirstName() != null) {
            userToEdit.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            userToEdit.setLastName(userDto.getLastName());
        }
        if (userDto.getAge() > 0) {
            userToEdit.setAge(userDto.getAge());
        }

        User updatedUser = userRepository.save(userToEdit);

        AppLogger.logInfo("Updated user with id: " + updatedUser.getId());


        return UserDto.builder()
                .userName(updatedUser.getUserName())
                .firstName(userDto.getFirstName())
                .lastName(updatedUser.getLastName())
                .age(updatedUser.getAge())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void blockUser(long id) {
        validateUserId(id);
        User userToBlock = findUserById(id);
        if (userToBlock.isBlocked()) {
            return;
        }
        userToBlock.setBlocked(true);
        userRepository.save(userToBlock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unblockUser(long id) {
        validateUserId(id);
        User userToBlock = findUserById(id);
        if (!userToBlock.isBlocked()) {
            return;
        }
        userToBlock.setBlocked(false);
        userRepository.save(userToBlock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPenaltyPoints(long userId, int maxPenaltyPoints, RentalStatus rentalStatus) {
        validateUserId(userId);
        User userToUpdate = findUserById(userId);
        switch (rentalStatus) {
            case DUE_TODAY -> userToUpdate.setPenaltyPoints(configService.getOverduePoints());
            case OVERDUE -> userToUpdate.setPenaltyPoints(configService.getLateOverduePoints());
        }
        if (userToUpdate.getPenaltyPoints() >= maxPenaltyPoints) {
            userToUpdate.setBlocked(true);
        }

        userRepository.save(userToUpdate);

    }

    @Override
    public UserDto getUserStatus(long userId) {
        validateUserId(userId);

        User user = findUserById(userId);

        AppLogger.logInfo("Collected data for user with id: " + user.getId());


        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .penaltyPoints(user.getPenaltyPoints())
                .isBlocked(user.isBlocked())
                .build();
    }

    @Override
    public List<UserDto> getAllUsers() {
        Iterable<User> usersIterable = userRepository.findAll();
        List<User> userList = StreamSupport.stream(usersIterable.spliterator(), false)
                .toList();

        AppLogger.logInfo("Found " + userList.size() + " users");
        return userList.stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .userName(user.getUserName())
                        .build()
                ).toList();
    }

    public User findUserById(long id) {
        validateUserId(id);
        return userRepository.findById(id).orElseThrow(
                () -> {
                    UserException userException = new UserException(UserError.USER_NOT_FOUND);
                    AppLogger.logError(userException.getError().getCode() + "for username: " + id);
                    return userException;
                }
        );
    }


    private void validateUserId(long userId) {
        if (userId <= 0) {
            AppLogger.logError("User id is null");
            throw new IllegalArgumentException("userId cannot be null");
        }
    }

}
