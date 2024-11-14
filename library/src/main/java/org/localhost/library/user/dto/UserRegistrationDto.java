package org.localhost.library.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserRegistrationDto {
    @NotBlank(message = "Username is required")
    private String userName;
    @NotBlank(message = "Firstname is required")
    private String firstName;
    @NotBlank(message = "Lastname is required")
    private String lastName;

    @NotNull(message = "Age is required")
    @Positive(message = "Age must be greater than 0")
    @Min(value = 15, message = "Age must be greater than {value}")
    private int age;
}
