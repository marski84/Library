package org.localhost.library.book.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.localhost.library.book.model.Book;

@Getter
@Builder
@ToString
public class BookDto {
    private long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private int pages;

    public static BookDto fromBook(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .isbn(book.getIsbn())
                .pages(book.getPages())
                .build();
    }
}
