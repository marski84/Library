package org.localhost.library.config;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class BaseConfigService implements ConfigService {
    private final LibraryParamsRepository libraryParamsRepository;

    public BaseConfigService(LibraryParamsRepository libraryParamsRepository) {
        this.libraryParamsRepository = libraryParamsRepository;
    }

    public int getMaxPenaltyPoints() {
        return getValue(ConfigKeys.MAX_PENALTY_POINTS);
    }

    public int getMaxUserRentals() {
        return getValue(ConfigKeys.MAX_RENTALS);
    }

    public int getRentalPeriodDays() {
        return getValue(ConfigKeys.RENTAL_PERIOD_DAYS);
    }

    public int getValue(String key) {
        return libraryParamsRepository.findById(key)
                .map(LibraryParams::getValue).orElseThrow();
    }

//    private BigDecimal getBigDecimalValue(String key, BigDecimal defaultValue) {
//        return libraryParamsRepository.findById(key)
//                .map(config -> new BigDecimal(config.getValue()));
//    }

    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(String key, int value) {
        LibraryParams config = libraryParamsRepository.findById(key).orElseThrow();
        config.setKey(key);
        config.setValue(value);
        config.setLastUpdated(ZonedDateTime.now());
        libraryParamsRepository.save(config);
    }
}