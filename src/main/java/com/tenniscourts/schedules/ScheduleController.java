package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value="/schedules")
@AllArgsConstructor
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @ApiOperation(value = "Create a Schedule.")
    @PostMapping
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO.getTennisCourtId(), createScheduleRequestDTO).getId())).build();
    }

    @ApiOperation(value = "Find a schedules by startDate and endDate.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid params.")})
    @GetMapping(value="/dates")
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(@RequestParam(value = "startDate") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
                                                                  @RequestParam(value = "endDate") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDate endDate) {

        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @ApiOperation(value = "Find a Schedule by Id.")
    @GetMapping(value="/{scheduleId}")
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
