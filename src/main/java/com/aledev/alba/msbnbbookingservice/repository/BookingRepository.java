package com.aledev.alba.msbnbbookingservice.repository;

import com.aledev.alba.msbnbbookingservice.domain.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingUid(UUID uuid);
}
