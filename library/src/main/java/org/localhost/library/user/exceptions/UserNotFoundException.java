package org.localhost.library.user.exceptions;

import org.localhost.library.user.exceptions.messages.UserExceptionMessages;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(UserExceptionMessages.USER_NOT_FOUND);
    }
}
