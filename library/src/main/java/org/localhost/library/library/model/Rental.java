package org.localhost.library.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.localhost.library.book.model.Book;
import org.localhost.library.user.model.User;

import java.time.ZonedDateTime;

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

    @NotNull
    private ZonedDateTime rentDate;

    @NotNull
    private ZonedDateTime dueDate;

    private ZonedDateTime returnDate;
    private int penaltyPointsForDue;

    public Rental() {
        this.penaltyPointsForDue = 0;
    }
}
