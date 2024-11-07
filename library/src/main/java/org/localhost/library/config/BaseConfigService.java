package org.localhost.library.config;


import org.localhost.library.config.properties.LibraryProperties;
import org.springframework.stereotype.Service;

@Service
public class BaseConfigService implements ConfigService {
    private final LibraryProperties libraryProperties;

    public BaseConfigService(LibraryProperties libraryProperties) {
        this.libraryProperties = libraryProperties;
    }

    public int getOverduePoints() {
        return libraryProperties.getOverduePenalty();
    }

    public int getLateOverduePoints() {
        return libraryProperties.getLateOverduePenalty();
    }

    @Override
    public int getMaxPenaltyPoints() {
        return libraryProperties.getMaxPenaltyPoints();
    }

    @Override
    public int getMaxUserRentals() {
        return libraryProperties.getMaxUserRentals();
    }

    @Override
    public int getRentalPeriodDays() {
        return libraryProperties.getRentalPeriodDays();
    }

    public boolean getIsRentalExtensionPossible() {
        return libraryProperties.isAllowRentalExtension();
    }

    public boolean getAreNotificationsActive() {
        return libraryProperties.isEnableNotifications();
    }

    public int getNotificationInterval() {
        if (getAreNotificationsActive()) {
            return libraryProperties.getOverdueCheckInterval();
        }
        throw new IllegalStateException("Notifications are not active");
    }

    public int getReminderDays() {
        if (getAreNotificationsActive()) {
            return libraryProperties.getReminderDays();
        }
        throw new IllegalStateException("Notifications are not active");
    }

}