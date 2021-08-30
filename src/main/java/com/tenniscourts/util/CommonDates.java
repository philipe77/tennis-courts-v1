package com.tenniscourts.util;

import java.time.LocalDateTime;

public class CommonDates {
    public LocalDateTime addHoursToDateTime(LocalDateTime startDate, Integer hour) {
        LocalDateTime after = startDate.plusHours(hour);
        return after;
    }
}
