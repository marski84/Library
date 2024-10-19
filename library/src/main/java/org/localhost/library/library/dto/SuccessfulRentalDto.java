package org.localhost.library.library.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Builder
@ToString
public class SuccessfulRentalDto {
    private String bookTitle;
    private String author;
    private String isbn;
    private long userId;
    private int rentalTime;
    private Instant rentalDate;
    private Instant dueDate;
}
