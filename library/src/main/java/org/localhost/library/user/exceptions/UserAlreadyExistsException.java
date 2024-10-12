package org.localhost.library.user.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super(ExceptionMessages.USER_EXISTS);
    }
}
