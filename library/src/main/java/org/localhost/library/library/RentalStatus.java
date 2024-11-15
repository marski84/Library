package org.localhost.library.library;

import lombok.Getter;

@Getter
public enum RentalStatus {
    OVERDUE(-1),
    DUE_TODAY(0),
    ON_TIME(1);

    private final int value;

    RentalStatus(int value) {
        this.value = value;
    }
}
