package com.tenniscourts.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CommonDates {
    public LocalDateTime addHoursToDateTime(LocalDateTime startDate, Integer hour) {
        LocalDateTime after = startDate.plusHours(hour);
        return after;
    }

    public boolean compareDifferentHours(Integer firstHour, Integer firstMinute, Integer lastHour, Integer lastMinute, LocalTime compareTime){
        LocalTime firstTime = LocalTime.of(firstHour, firstMinute);
        LocalTime lastTime = LocalTime.of(lastHour, lastMinute);

        if (compareTime.isAfter(firstTime) && compareTime.isBefore(lastTime)) {
            return true;
        }
        return false;
    }
}
