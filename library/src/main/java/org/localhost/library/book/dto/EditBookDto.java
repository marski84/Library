package org.localhost.library.book.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class EditBookDto {
    private String title;
    private String author;
    private String publisher;
    private int pages;
}
