package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.schedules.*;
import com.tenniscourts.util.CommonDates;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private final ReservationMapper reservationMapper;
    private final ScheduleMapper scheduleMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationsRequestDTO) {
        ScheduleDTO scheduleDTO = scheduleService.findSchedule(createReservationsRequestDTO.getScheduleId());
        Guest guest = guestService.findGuestById(createReservationsRequestDTO.getGuestId());

        hasReservationAlreadyMade(scheduleMapper.map(scheduleDTO), guest);

        Reservation createReservation = new Reservation(
                guest, scheduleMapper.map(scheduleDTO), BigDecimal.valueOf(10.0), ReservationStatus.READY_TO_PLAY, BigDecimal.ZERO);
        reservationRepository.save(createReservation);

        Schedule schedule = scheduleMapper.map(scheduleDTO);
        List<Reservation> listReservations = new ArrayList<>();
        listReservations.add(createReservation);
        schedule.setReservations(listReservations);
        scheduleRepository.save(schedule);

        return reservationMapper.map(createReservation);
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        LocalTime now =  LocalTime.now();
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);
            Reservation reservationFee = reservationFee(reservation, now);
            BigDecimal refundValue = getRefundValue(reservationFee);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours >= 24 || reservation.getSchedule().getEndDateTime().isAfter(LocalDateTime.now())) {
            return reservation.getValue();
        }

        return BigDecimal.ZERO;
    }

    /*TODO: This method actually not fully working, find a way to fix the issue when it's throwing the error:
            "Cannot reschedule to the same slot.*/
    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        Reservation previousReservation = cancel(previousReservationId);
        LocalTime now =  LocalTime.now();
        if (scheduleId.equals(previousReservation.getSchedule().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        previousReservation = reservationFee(previousReservation, now);
        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }

    private void hasReservationAlreadyMade(Schedule schedule, Guest guest) {
        var reservationExists = reservationRepository.findBySchedule_IdAndGuest_Id(schedule.getId(), guest.getId());
        if(reservationExists.isPresent()) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }
    }

    private Reservation reservationFee(Reservation reservation, LocalTime cancelTime) {
        CommonDates commonDates = new CommonDates();
        if(commonDates.compareDifferentHours(12,00, 23, 59, cancelTime)) {
            reservation.setRefundValue(reservation.getValue().multiply(BigDecimal.valueOf(0.25)));
        } else if(commonDates.compareDifferentHours(02,00, 11, 59, cancelTime)){
            reservation.setRefundValue(reservation.getValue().multiply(BigDecimal.valueOf(0.50)));
        } else if(commonDates.compareDifferentHours(00,01, 02, 00, cancelTime)) {
            reservation.setRefundValue(reservation.getValue().multiply(BigDecimal.valueOf(0.75)));
        }
        return reservation;
    }
}
