package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "Create a guest")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Guest was created successfully."),
            @ApiResponse(code = 400, message = "Invalid params.")})
    @PostMapping
    public ResponseEntity<Void> createGuest(@RequestBody GuestDTO guest) {
        return ResponseEntity.created(locationByEntity(guestService.createGuest(guest).getId())).build();
    }

    @ApiOperation(value = "Update a guest")
    @PutMapping
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Guest was updated successfully."),
            @ApiResponse(code = 400, message = "Invalid params.")})
    public ResponseEntity<Void> updateGuest(@RequestBody GuestDTO guest) {
        return ResponseEntity.created(locationByEntity(guestService.updateGuest(guest).getId())).build();
    }

    @ApiOperation(value = "Delete a guest")
    @DeleteMapping(value="/{guestId}")
    public ResponseEntity<?> deleteGuest(@PathVariable Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "List all guests")
    @GetMapping
    public ResponseEntity<List<GuestDTO>> listAllGuests() {
        return ResponseEntity.ok().body(guestService.findAllGuests());
    }

    @ApiOperation(value = "Find a guests by name")
    @GetMapping(value = "/name")
    public ResponseEntity<List<GuestDTO>> findGuestByName(@RequestBody Guest guest) {
        return ResponseEntity.ok().body(guestService.findByName(guest.getName()));
    }

}
