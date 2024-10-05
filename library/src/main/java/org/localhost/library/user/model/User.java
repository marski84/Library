package org.localhost.library.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.localhost.library.book.model.Book;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", indexes = {@Index(name = "idx_users_user_name", columnList = "user_name")}, uniqueConstraints = {@UniqueConstraint(name = "uq_users_user_name", columnNames = {"user_name"})})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private int penaltyPoints = 0;
    private boolean isBlocked = false;

    @OneToMany(mappedBy = "user")
    private List<Book> books = new ArrayList<>();


    public void blockUser() {
        isBlocked = true;
    }

    public void unblockUser() {
        isBlocked = false;
    }

    public void increasePenaltyPoints() {
        penaltyPoints++;
    }

    public void decreasePenaltyPoints() {
        if (penaltyPoints > 0) {
            penaltyPoints--;
        }
    }
}
