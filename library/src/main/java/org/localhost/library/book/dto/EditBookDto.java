package org.localhost.library.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EditBookDto {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    @Min(value = 1, message = "Pages must be greater than 0")
    private Integer pages;

    @NotBlank(message = "Publisher cannot be blank")
    private String publisher;
}
