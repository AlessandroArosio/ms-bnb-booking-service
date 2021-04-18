package com.aledev.alba.msbnbbookingservice.service;

import com.aledev.alba.msbnbbookingservice.domain.BookingPagedList;
import com.aledev.alba.msbnbbookingservice.web.model.BookingDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookingService {
    BookingPagedList listBookings(Pageable pageable);
    BookingDto getBookingById(Long id);
    BookingDto getBookingByUUID(UUID uuid);
    BookingDto newBooking(BookingDto dto);
    BookingDto updateBooking(Long id, BookingDto dto);
    void deleteBooking(Long id);
}
