package org.localhost.library.library.services;

import org.localhost.library.config.ConfigService;

public class InMemoryConfigService implements ConfigService {


    public InMemoryConfigService() {}

    @Override
    public int getMaxPenaltyPoints() {
        return 3;
    }

    @Override
    public int getMaxUserRentals() {
        return 2;
    }

    @Override
    public int getRentalPeriodDays() {
        return 10;
    }

    @Override
    public boolean getIsRentalExtensionPossible() {
        return false;
    }

    @Override
    public boolean getAreNotificationsActive() {
        return false;
    }

    @Override
    public int getNotificationInterval() {
        return 0;
    }

    @Override
    public int getReminderDays() {
        return 0;
    }

    @Override
    public int getOverduePoints() {
        return 5;
    }

    @Override
    public int getLateOverduePoints() {
        return 10;
    }
}
