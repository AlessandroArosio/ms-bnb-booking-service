package com.aledev.alba.msbnbbookingservice.bootstrap;

import com.aledev.alba.msbnbbookingservice.domain.entity.Booking;
import com.aledev.alba.msbnbbookingservice.domain.entity.Room;
import com.aledev.alba.msbnbbookingservice.domain.enums.Property;
import com.aledev.alba.msbnbbookingservice.repository.BookingRepository;
import com.aledev.alba.msbnbbookingservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (roomRepository.count() == 0) {
            var rooms = populateRooms();
            populateBookings(rooms);
        }

    }

    private void populateBookings(List<Room> rooms) {
        List<Room> all = roomRepository.findAll();
        bookingRepository.save(Booking.builder()
                .roomsBooked(all)
                .bookingUid(UUID.randomUUID())
                .checkin(LocalDateTime.of(2021, 9, 10, 15, 0))
                .checkout(LocalDateTime.of(2021, 9, 13, 10, 0))
                .bookingAmount(new BigDecimal("52.50"))
                .customerId(6L)
                .isPaid(false)
                .hasAddons(false)
                .confirmationCode("ABC1")
                .build());

        bookingRepository.save(Booking.builder()
                .roomsBooked(List.of(all.get(0)))
                .bookingUid(UUID.randomUUID())
                .checkin(LocalDateTime.of(2021, 10, 1, 15, 0))
                .checkout(LocalDateTime.of(2021, 10, 5, 10, 0))
                .bookingAmount(new BigDecimal("122.75"))
                .customerId(8L)
                .isPaid(false)
                .hasAddons(false)
                .confirmationCode("ABC2")
                .build());

        bookingRepository.save(Booking.builder()
                .roomsBooked(List.of(all.get(1)))
                .bookingUid(UUID.randomUUID())
                .checkin(LocalDateTime.of(2021, 10, 7, 15, 0))
                .checkout(LocalDateTime.of(2021, 10, 8, 10, 0))
                .bookingAmount(new BigDecimal("21.90"))
                .customerId(2L)
                .isPaid(false)
                .hasAddons(true)
                .confirmationCode("ABC3")
                .build());
    }

    private List<Room> populateRooms() {
        return roomRepository.saveAll(List.of(
                Room.builder()
                        .roomName("Bedroom 3")
                        .property(Property.FERRIER_MEDWAY)
                        .capacity((short) 2)
                        .roomType("Double")
                        .build(),
                Room.builder()
                        .roomName("Test room X")
                        .property(Property.FERRIER_MEDWAY)
                        .capacity((short) 3)
                        .roomType("Triple")
                        .build()));
    }
}
