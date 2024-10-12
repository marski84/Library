package org.localhost.library.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisteredUserDto {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private int age;
    private int penaltyPoints;
    private boolean isBlocked;
}
