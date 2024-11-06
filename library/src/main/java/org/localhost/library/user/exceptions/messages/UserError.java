package org.localhost.library.user.exceptions.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserError {
    USER_EXISTS(2000, "User already exists!"),
    USER_NOT_FOUND(2100, "User not found");

    private final int code;
    private final String message;
}
