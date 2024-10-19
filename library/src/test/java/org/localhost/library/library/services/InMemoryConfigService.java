package org.localhost.library.library.services;

import org.localhost.library.config.ConfigService;
import org.localhost.library.config.LibraryParamsRepository;

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
    public int getValue(String key) {
        return 0;
    }
}
