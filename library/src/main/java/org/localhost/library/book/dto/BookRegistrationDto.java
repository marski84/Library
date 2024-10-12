package org.localhost.library.book.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookRegistrationDto {
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private int pages;
}
