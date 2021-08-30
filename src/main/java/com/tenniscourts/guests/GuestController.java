package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value="/guests")
@AllArgsConstructor
@Secured("ADMIN")
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

    @GetMapping
    public ResponseEntity<List<GuestDTO>> listAllGuests() {
        return ResponseEntity.ok().body(guestService.findAllGuests());
    }

    @GetMapping(value = "/name")
    public ResponseEntity<List<GuestDTO>> findGuestByName(@RequestBody Guest guest) {
        return ResponseEntity.ok().body(guestService.findByName(guest.getName()));
    }

}
