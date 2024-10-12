package org.localhost.library.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRegistrationDto {
    private String userName;
    private String firstName;
    private String lastName;
    private int age;
}
