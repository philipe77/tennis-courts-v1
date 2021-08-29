package com.tenniscourts.guests;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;

@Service
@AllArgsConstructor
public class GuestService {

    @Autowired
    GuestRepository guestRepository;
    private final GuestMapper guestMapper;


    public Guest findGuestById(Long guestId) {
        return guestRepository.findById(guestId).orElseThrow(()-> new NotFoundException(
                "Object not found! Id: " + guestId + " Type: " + Guest.class.getName()));
    }

    public GuestDTO createGuest(GuestDTO guest) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guest)));
    }

    public GuestDTO updateGuest(GuestDTO guest) {
        Guest findGuest = findGuestById(guest.getId());
        findGuest.setName(guest.getName());
        return guestMapper.map(guestRepository.saveAndFlush(findGuest));
    }
}
