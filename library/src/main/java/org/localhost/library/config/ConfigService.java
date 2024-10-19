package org.localhost.library.config;

public interface ConfigService {
    int getMaxPenaltyPoints();
    int getMaxUserRentals();
    int getRentalPeriodDays();
    int getValue(String key);
}
