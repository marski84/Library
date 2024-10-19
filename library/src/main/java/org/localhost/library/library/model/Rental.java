package org.localhost.library.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.localhost.library.book.model.Book;
import org.localhost.library.user.model.User;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Instant rentDate;
    private Instant dueDate;
    private Instant returnDate;
    private int penaltyPointsForDue;

    public Rental() {
        this.penaltyPointsForDue = 0;
    }
}
