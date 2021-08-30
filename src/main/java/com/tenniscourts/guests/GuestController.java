package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.config.persistence.Profile;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

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

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<GuestDTO>> listAllGuests() {
        return ResponseEntity.ok().body(guestService.findAllGuests());
    }

    @GetMapping(value = "/name")
    public ResponseEntity<List<GuestDTO>> findGuestByName(@RequestBody Guest guest) {
        return ResponseEntity.ok().body(guestService.findByName(guest.getName()));
    }

}
