package org.localhost.library.book.dto;

import lombok.Builder;
import lombok.Getter;
import org.localhost.library.user.model.User;

@Getter
@Builder
public class BookDto {
    private long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private int pages;
}
