package com.aledev.alba.msbnbbookingservice.web.controller;

import com.aledev.alba.msbnbbookingservice.domain.BookingPagedList;
import com.aledev.alba.msbnbbookingservice.service.BookingService;
import com.aledev.alba.msbnbbookingservice.web.model.BookingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BookingService bookingService;

    @GetMapping
    public BookingPagedList listBookings(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return bookingService.listBookings(PageRequest.of(pageNumber, pageSize));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto placeBooking(@RequestBody @Valid BookingDto bookingDto) {
        return bookingService.newBooking(bookingDto);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    @GetMapping("/uuid/{bookingUUID}")
    public BookingDto getBookingByUuId(@PathVariable UUID bookingUUID) {
        return bookingService.getBookingByUUID(bookingUUID);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateBooking(@PathVariable Long id, @RequestBody BookingDto bookingDto) {
        return bookingService.updateBooking(id, bookingDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}
