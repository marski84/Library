package org.localhost.library.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.localhost.library.book.model.Book;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
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

    @Column(nullable = false)
    private int age;

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
