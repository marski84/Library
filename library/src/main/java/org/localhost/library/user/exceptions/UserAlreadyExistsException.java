package org.localhost.library.user.exceptions;

import org.localhost.library.user.exceptions.messages.UserExceptionMessages;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super(UserExceptionMessages.USER_EXISTS);
    }
}
