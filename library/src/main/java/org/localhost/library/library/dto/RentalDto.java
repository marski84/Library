package org.localhost.library.library.dto;

import lombok.Builder;
import lombok.Getter;
import org.localhost.library.library.model.Rental;

import java.time.Duration;
import java.time.ZonedDateTime;

@Builder
@Getter
public class RentalDto {
    private long id;
    private long bookId;
    private String bookTitle;
    private String author;
    private String isbn;
    private long userId;
    private long rentalTime;
    private ZonedDateTime rentalDate;
    private ZonedDateTime dueDate;
    private ZonedDateTime returnDate;

    public static RentalDto fromRental(Rental rental) {
        if (rental.getRentDate() != null && rental.getDueDate() != null) {
            long rentalTime = Duration.between(
                    rental.getRentDate(),
                    rental.getDueDate()
            ).toDays();

            return RentalDto.builder()
                    .id(rental.getId())
                    .bookId(rental.getBook().getId())
                    .bookTitle(rental.getBook().getTitle())
                    .author(rental.getBook().getAuthor())
                    .isbn(rental.getBook().getIsbn())
                    .userId(rental.getUser().getId())
                    .rentalTime(rentalTime)
                    .rentalDate(rental.getRentDate())
                    .dueDate(rental.getDueDate())
                    .returnDate(rental.getReturnDate())
                    .build();
        }
        return RentalDto.builder()
                .id(rental.getId())
                .bookTitle(rental.getBook().getTitle())
                .author(rental.getBook().getAuthor())
                .isbn(rental.getBook().getIsbn())
                .bookTitle(rental.getBook().getTitle())
                .userId(rental.getUser().getId())
                .rentalDate(rental.getRentDate())
                .dueDate(rental.getDueDate())
                .returnDate(rental.getReturnDate())
                .build();
    }
}
