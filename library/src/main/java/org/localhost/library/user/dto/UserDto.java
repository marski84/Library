package org.localhost.library.user.dto;

import lombok.*;
import org.localhost.library.user.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private int age;
    private int penaltyPoints;
    private boolean isBlocked;

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .penaltyPoints(user.getPenaltyPoints())
                .isBlocked(user.isBlocked())
                .build();
    }
}