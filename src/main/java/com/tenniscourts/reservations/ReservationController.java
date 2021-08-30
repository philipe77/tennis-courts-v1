package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/reservations")
@AllArgsConstructor
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @ApiOperation(value = "Book a reservation.")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Reservation successfully booked.")})
    @PostMapping
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @ApiOperation(value = "Find reservation by Id.")
    @GetMapping
    public ResponseEntity<ReservationDTO> findReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @ApiOperation(value = "Cancel reservation by Id.")
    @PutMapping(value = "{/cancel/{reservationId}}")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @ApiOperation(value = "Reschedule reservation by Id.")
    @PutMapping(value = "{/reschedule/{reservationId}}")
    public ResponseEntity<ReservationDTO> rescheduleReservation(Long reservationId, Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
