package com.aledev.alba.msbnbbookingservice.service;

import com.aledev.alba.msbnbbookingservice.domain.Booking;
import com.aledev.alba.msbnbbookingservice.domain.BookingPagedList;
import com.aledev.alba.msbnbbookingservice.repository.BookingRepository;
import com.aledev.alba.msbnbbookingservice.web.mappers.BookingMapper;
import com.aledev.alba.msbnbbookingservice.web.model.BookingDto;
import com.aledev.alba.msbnbbookingservice.web.model.BookingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingPagedList listBookings(Pageable pageable) {
        Page<Booking> bookingPage = bookingRepository.findAll(pageable);

        return new BookingPagedList(bookingPage.stream()
                .map(bookingMapper::bookingToDto)
                .collect(Collectors.toList()), PageRequest.of(
                bookingPage.getPageable().getPageNumber(),
                bookingPage.getPageable().getPageSize()),
                bookingPage.getTotalElements());
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(BookingException::new);
        BookingDto bookingDto = bookingMapper.bookingToDto(booking);
        if (bookingDto.getHasAddons()) {
//            TODO
//            List<Addon> extras = addonService.findExtras(bookingDto.getBookingUid());
//            bookingDto.getExtras().setAddonList(extras);
            return bookingDto;
        }
        return bookingDto;
    }

    @Override
    public BookingDto getBookingByUUID(UUID uuid) {
        Booking booking = bookingRepository.findByBookingUid(uuid).orElseThrow(BookingException::new);
        return null;
    }

    @Override
    public BookingDto newBooking(BookingDto dto) {
        return null;
    }

    @Override
    public BookingDto updateBooking(Long id, BookingDto dto) {
        return null;
    }

    @Override
    public void deleteBooking(Long id) {

    }
}
