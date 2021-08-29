package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import com.tenniscourts.tenniscourts.TennisCourtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/guests")
@AllArgsConstructor
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @PostMapping
    public ResponseEntity<Void> createGuest(@RequestBody GuestDTO guest) {
        return ResponseEntity.created(locationByEntity(guestService.createGuest(guest).getId())).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateGuest(@RequestBody GuestDTO guest) {
        return ResponseEntity.created(locationByEntity(guestService.updateGuest(guest).getId())).build();
    }

    @DeleteMapping(value="/{guestId}")
    public ResponseEntity<?> deleteGuest(@PathVariable Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }


}
