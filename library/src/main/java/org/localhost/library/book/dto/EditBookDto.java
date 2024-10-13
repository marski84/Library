package org.localhost.library.book.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditBookDto {
    private String title;
    private String author;
    private String publisher;
    private int pages;
}
