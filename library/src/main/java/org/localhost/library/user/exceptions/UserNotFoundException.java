package org.localhost.library.user.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(ExceptionMessages.USER_NOT_FOUND);
    }
}
