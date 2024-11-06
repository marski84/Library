package org.localhost.library.user.exceptions;

import lombok.Getter;
import org.localhost.library.user.exceptions.messages.UserError;

@Getter
public class UserException extends RuntimeException {
    private final UserError error;

    public UserException(UserError error) {
        super(error.getMessage());
        this.error = error;
    }
}
