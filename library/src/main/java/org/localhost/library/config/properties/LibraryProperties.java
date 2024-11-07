package org.localhost.library.config.properties;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "library")
@Validated
@Getter
@Setter
public class LibraryProperties {
    @Min(1)
    private int maxPenaltyPoints;
    @Min(1)
    private int maxUserRentals;
    @Min(1)
    private int rentalPeriodDays;
    @Min(1)
    private int overduePenalty;
    @Min(1)
    private int lateOverduePenalty;

    private boolean allowRentalExtension = true;
    private boolean enableNotifications = true;

    @Min(1)
    private int reminderDays;
    @Min(1)
    private int overdueCheckInterval;
}
