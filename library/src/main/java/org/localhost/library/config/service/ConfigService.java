package org.localhost.library.config.service;

public interface ConfigService {
    int getMaxPenaltyPoints();
    int getMaxUserRentals();
    int getRentalPeriodDays();

    boolean getIsRentalExtensionPossible();

    boolean getAreNotificationsActive();

    int getNotificationInterval();

    int getReminderDays();

    int getOverduePoints();

    int getLateOverduePoints();
}
