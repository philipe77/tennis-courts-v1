package com.tenniscourts.guests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByNameContaining(String name);
}
