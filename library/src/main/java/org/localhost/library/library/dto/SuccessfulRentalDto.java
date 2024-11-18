package org.localhost.library.library.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.localhost.library.library.model.Rental;

import java.time.Duration;
import java.time.ZonedDateTime;

@Getter
@Builder
@ToString
public class SuccessfulRentalDto {
    private long rentalId;
    private String bookTitle;
    private String author;
    private String isbn;
    private long userId;
    private long rentalTime;
    private ZonedDateTime rentalDate;
    private ZonedDateTime dueDate;

    public static SuccessfulRentalDto fromRental(Rental rental) {
        long rentalTime = Duration.between(
                rental.getRentDate(),
                rental.getDueDate()
        ).toDays();
        return SuccessfulRentalDto.builder()
                .rentalId(rental.getId())
                .bookTitle(rental.getBook().getTitle())
                .author(rental.getBook().getAuthor())
                .isbn(rental.getBook().getIsbn())
                .userId(rental.getUser().getId())
                .rentalTime(rentalTime)
                .rentalDate(rental.getRentDate())
                .dueDate(rental.getDueDate())
                .build();
    }

}
