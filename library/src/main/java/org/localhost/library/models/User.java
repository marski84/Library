package org.localhost.library.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "client")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    private int penaltyPoints;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Book borrowedBook;
}
