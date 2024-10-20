package org.localhost.library.library.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public class RentalDto {
    private String bookTitle;
    private String author;
    private String isbn;
    private long userId;
    private int rentalTime;
    private Instant rentalDate;
    private Instant dueDate;
}
