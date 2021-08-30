package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.Reservation;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtService;
import com.tenniscourts.util.CommonDates;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {

    @Autowired
    private final ScheduleRepository scheduleRepository;

    @Autowired
    private final TennisCourtService tennisCourtService;

    private final ScheduleMapper scheduleMapper;
    private final TennisCourtMapper tennisCourtMapper;

    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        CommonDates common = new CommonDates();
        LocalDateTime startDate = createScheduleRequestDTO.getStartDateTime();
        LocalDateTime endDate = common.addHoursToDateTime(startDate, 1);
        TennisCourt tennisCourt = tennisCourtMapper.map(tennisCourtService.findTennisCourtById(tennisCourtId));
        List<Schedule> schedules = scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(createScheduleRequestDTO.getTennisCourtId());
        for (Schedule  item : schedules) {
            System.out.println(item.getEndDateTime());
            System.out.println(endDate);
            if (startDate.isBefore(item.getEndDateTime())) {
                throw new AlreadyExistsEntityException("Already exists schedule for this period.");
            }
        }
        List<Reservation> reservations = new ArrayList<>();
        Schedule schedule = new Schedule(tennisCourt, startDate, endDate, reservations);
        return scheduleMapper.map(scheduleRepository.saveAndFlush(schedule));
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<Schedule> schedules = scheduleRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqualOrderByStartDateTime(startDate, endDate);
        if(schedules.isEmpty()) {
            throw new EntityNotFoundException("Not exists schedules for this period.");
        }
        List<ScheduleDTO> listSchedulesDTO = scheduleMapper.map(schedules);
        return listSchedulesDTO;

    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Schedules not found.");
        });
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
