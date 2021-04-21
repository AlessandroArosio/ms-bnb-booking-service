package com.aledev.alba.msbnbbookingservice.service;

import com.aledev.alba.msbnbbookingservice.domain.entity.Booking;
import com.aledev.alba.msbnbbookingservice.domain.entity.Room;
import com.aledev.alba.msbnbbookingservice.domain.enums.AddonCategory;
import com.aledev.alba.msbnbbookingservice.domain.enums.AddonType;
import com.aledev.alba.msbnbbookingservice.domain.enums.Property;
import com.aledev.alba.msbnbbookingservice.domain.exceptions.BookingException;
import com.aledev.alba.msbnbbookingservice.repository.BookingRepository;
import com.aledev.alba.msbnbbookingservice.service.addon.AddonService;
import com.aledev.alba.msbnbbookingservice.web.mappers.BookingMapper;
import com.aledev.alba.msbnbbookingservice.web.model.Addon;
import com.aledev.alba.msbnbbookingservice.web.model.BookingDto;
import com.aledev.alba.msbnbbookingservice.web.model.Extras;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BookingServiceImplTest {
    @Mock
    BookingMapper mapper;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    AddonService addonService;

    @Captor
    ArgumentCaptor<Booking> captor;

    @InjectMocks
    BookingServiceImpl bookingService;

    static Booking booking;
    static BookingDto dto;
    static String testUuid = "7642c92b-4096-4515-a308-56f09ae30ed8";

    @BeforeEach
    void setUp() {
        dto = BookingDto.builder()
                .id(1L)
                .bookingUid(UUID.fromString(testUuid))
                .bookingAmount(new BigDecimal("45.00"))
                .customerId(50L)
                .hasAddons(false)
                .isPaid(false)
                .checkin(LocalDateTime.of(2021, 7, 15, 14, 0))
                .checkout(LocalDateTime.of(2021, 7, 17, 10, 0))
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .roomsBooked(List.of(Room.builder()
                        .id(22L)
                        .capacity((short)2)
                        .property(Property.FERRIER_MEDWAY)
                        .roomName("Bedroom3")
                        .roomType("Double")
                        .build()))
                .build();

        booking = Booking.builder()
                .id(1L)
                .bookingUid(UUID.fromString(testUuid))
                .bookingAmount(new BigDecimal("45.00"))
                .customerId(50L)
                .hasAddons(false)
                .isPaid(false)
                .checkin(LocalDateTime.of(2021, 7, 15, 14, 0))
                .checkout(LocalDateTime.of(2021, 7, 17, 10, 0))
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .roomsBooked(List.of(Room.builder()
                        .id(22L)
                        .capacity((short)2)
                        .property(Property.FERRIER_MEDWAY)
                        .roomName("Bedroom3")
                        .roomType("Double")
                        .build()))
                .build();
    }

    @Test
    void listBookings_returnsPagedListTest() {
    }

    @Test
    void getBookingByIdTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(mapper.bookingToDto(any())).thenReturn(dto);

        BookingDto bookingDto = bookingService.getBookingById(1L);

        assertThat(bookingDto).isNotNull();
        assertThat(bookingDto.getCheckin()).isEqualTo(dto.getCheckin());
    }

    @Test
    void getBookingById_ThrowsExceptionIfNotFoundTest() {
        assertThrows(BookingException.class, () -> bookingService.getBookingById(1L));
    }

    @Test
    void getBookingByUUIDNoExtras() {
        when(bookingRepository.findByBookingUid(any())).thenReturn(Optional.of(booking));
        when(mapper.bookingToDto(any())).thenReturn(dto);

        BookingDto bookingDto = bookingService.getBookingByUUID(UUID.randomUUID());

        assertThat(bookingDto).isNotNull();
        assertThat(bookingDto.getCustomerId()).isEqualTo(dto.getCustomerId());
    }

    @Test
    void getBookingByUUIDWithExtras() {
        var extra = Extras.builder()
                .addonList(List.of(new Addon(AddonCategory.BREAKFAST, AddonType.CROISSANT, BigDecimal.ONE, 2)))
                .isPaid(true)
                .build();
        dto.setHasAddons(true);
        when(bookingRepository.findByBookingUid(any())).thenReturn(Optional.of(booking));
        when(mapper.bookingToDto(any())).thenReturn(dto);
        when(addonService.getAllAddonsForBookingUUID(any())).thenReturn(extra);

        BookingDto bookingDto = bookingService.getBookingByUUID(UUID.randomUUID());

        // TODO add extras

        assertThat(bookingDto.getHasAddons()).isTrue();
        assertThat(bookingDto.getCustomerId()).isEqualTo(dto.getCustomerId());
    }

    @Test
    void getBookingByUUID_ThrowsExceptionIfNotFoundTest() {
        UUID uuid = UUID.fromString(testUuid);
        assertThrows(BookingException.class, () -> bookingService.getBookingByUUID(uuid));
    }

    @Test
    void newBooking_returnDtoTest() {
        dto.setBookingUid(null);
        var newBooking = new Booking();

        when(mapper.dtoToBooking(dto)).thenReturn(newBooking);
        when(mapper.bookingToDto(booking)).thenReturn(dto);

        bookingService.newBooking(dto);

        verify(bookingRepository).save(captor.capture());

        Booking capturedValue = captor.getValue();

        assertThat(capturedValue.getBookingUid()).isNotNull();
    }

    @Test
    void updateBookingTest() {
        dto.setIsPaid(true);

        when(mapper.dtoToBooking(dto)).thenReturn(booking);
        when(mapper.bookingToDto(booking)).thenReturn(dto);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        bookingService.updateBooking(1L, dto);

        verify(bookingRepository).save(captor.capture());

        Booking capturedValue = captor.getValue();

        assertThat(capturedValue.getIsPaid()).isTrue();
    }

    @Test
    void updateBooking_ThrowsExceptionIfNotFoundTest() {
        assertThrows(BookingException.class, () -> bookingService.updateBooking(2L, dto));
    }

    @Test
    void deleteBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        bookingService.deleteBooking(1L);

        verify(bookingRepository, times(1)).delete(any());
    }
}