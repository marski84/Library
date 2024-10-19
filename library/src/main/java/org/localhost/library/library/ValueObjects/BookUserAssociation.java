package org.localhost.library.library.ValueObjects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.localhost.library.book.utils.AppLogger;

@Getter
@EqualsAndHashCode
@ToString
public final class BookUserAssociation {
    private final long bookId;
    private final long userId;

    public BookUserAssociation(long bookId, long userId) {
        this.bookId = bookId;
        this.userId = userId;
    }

    private static void validateIds(long bookId, long userId) {
        if (bookId <= 0 || userId <= 0) {
            AppLogger.logError("Invalid input data - Book ID: " + bookId + ", User ID: " + userId);
            throw new IllegalArgumentException("BookId and UserId must both be greater than 0");
        }
    }
}