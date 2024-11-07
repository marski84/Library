package org.localhost.library.config;

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
