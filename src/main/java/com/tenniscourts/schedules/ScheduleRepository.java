package com.tenniscourts.schedules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTennisCourt_IdOrderByStartDateTime(Long id);

    //@Query(value = "from Schedule sc where startDate BETWEEN :startDate AND :endDate")
    List<Schedule> findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqualOrderByStartDateTime(LocalDateTime endDate, LocalDateTime startDate);

}