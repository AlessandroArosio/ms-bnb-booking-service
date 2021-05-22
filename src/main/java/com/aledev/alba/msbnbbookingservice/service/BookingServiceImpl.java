package com.aledev.alba.msbnbbookingservice.service;

import com.aledev.alba.msbnbbookingservice.domain.BookingPagedList;
import com.aledev.alba.msbnbbookingservice.domain.entity.Booking;
import com.aledev.alba.msbnbbookingservice.domain.exceptions.BookingException;
import com.aledev.alba.msbnbbookingservice.repository.BookingRepository;
import com.aledev.alba.msbnbbookingservice.service.addon.AddonService;
import com.aledev.alba.msbnbbookingservice.web.mappers.BookingMapper;
import com.aledev.alba.msbnbbookingservice.web.model.BookingDto;
import com.aledev.alba.msbnbbookingservice.web.model.Extras;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final AddonService addonService;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingPagedList listBookings(Pageable pageable) {
        Page<Booking> bookingPage = bookingRepository.findAll(pageable);

        return new BookingPagedList(bookingPage.stream()
                .map(booking -> {
                    var dto = bookingMapper.bookingToDto(booking);
                    checkAndAddExtrasIfAvailable(dto);

                    return dto;
                })
                .collect(Collectors.toList()), PageRequest.of(
                bookingPage.getPageable().getPageNumber(),
                bookingPage.getPageable().getPageSize()),
                bookingPage.getTotalElements());
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingException(String.format("Booking ID %s: not found", id)));
        BookingDto bookingDto = bookingMapper.bookingToDto(booking);
        checkAndAddExtrasIfAvailable(bookingDto);

        return bookingDto;
    }

    @Override
    public BookingDto getBookingByUUID(UUID uuid) {
        var booking = bookingRepository.findByBookingUid(uuid)
                .orElseThrow(() -> new BookingException(String.format("Booking UUID %s: not found", uuid)));

        BookingDto bookingDto = bookingMapper.bookingToDto(booking);
        checkAndAddExtrasIfAvailable(bookingDto);

        return bookingDto;
    }

    @Override
    public BookingDto newBooking(BookingDto dto) {
        Booking booking = bookingMapper.dtoToBooking(dto);
        booking.setBookingUid(UUID.randomUUID());

        log.warn("BookingUUID: {}", booking.getBookingUid());

        return bookingMapper.bookingToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateBooking(Long id, BookingDto dto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingException(String.format("Booking ID %s: not found", id)));

        booking.setBookingAmount(dto.getBookingAmount());
        booking.setCheckin(dto.getCheckin());
        booking.setCheckout(dto.getCheckout());
        booking.setCustomerId(dto.getCustomerId());
        booking.setHasAddons(dto.getHasAddons());
        booking.setIsPaid(dto.getIsPaid());
        booking.setRoomsBooked(dto.getRoomsBooked());

        return bookingMapper.bookingToDto(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        booking.ifPresent(bookingRepository::delete);
    }

    private void checkAndAddExtrasIfAvailable(BookingDto bookingDto) {
        if (bookingDto.getHasAddons()) {
            Optional<Extras> optExtra = addonService.getAllAddonsForBookingUUID(bookingDto.getBookingUid());
            optExtra.ifPresent(bookingDto::setExtras);
        }
    }
}
