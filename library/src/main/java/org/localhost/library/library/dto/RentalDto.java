package org.localhost.library.library.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Builder
@Getter
public class RentalDto {
    private String bookTitle;
    private String author;
    private String isbn;
    private long userId;
    private int rentalTime;
    private ZonedDateTime rentalDate;
    private ZonedDateTime dueDate;
    private ZonedDateTime returnDate;
}
