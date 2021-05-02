package com.aledev.alba.msbnbbookingservice.service;

import com.aledev.alba.msbnbbookingservice.domain.entity.Booking;
import com.aledev.alba.msbnbbookingservice.domain.entity.Room;
import com.aledev.alba.msbnbbookingservice.domain.enums.AddonCategory;
import com.aledev.alba.msbnbbookingservice.domain.enums.AddonType;
import com.aledev.alba.msbnbbookingservice.domain.enums.Property;
import com.aledev.alba.msbnbbookingservice.repository.BookingRepository;
import com.aledev.alba.msbnbbookingservice.service.addon.AddonServiceImpl;
import com.aledev.alba.msbnbbookingservice.web.model.Addon;
import com.aledev.alba.msbnbbookingservice.web.model.BookingDto;
import com.aledev.alba.msbnbbookingservice.web.model.Extras;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class BookingServiceImplIT {

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    BookingService bookingService;

    @Autowired
    ObjectMapper objectMapper;

    static String testUuid = "7642c92b-4096-4515-a308-56f09ae30ed8";

    @TestConfiguration
    static class RestTemplateBuilderProvider {

        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer() {
            WireMockServer server = with(wireMockConfig().port(8081));
            server.start();
            return server;
        }
    }

    @BeforeAll
    static void beforeAll(@Autowired BookingRepository bookingRepository) {
        bookingRepository.save(Booking.builder()
                .bookingUid(UUID.fromString(testUuid))
                .bookingAmount(new BigDecimal("45.00"))
                .customerId(50L)
                .hasAddons(true)
                .isPaid(false)
                .checkin(LocalDateTime.of(2021, 7, 15, 14, 0))
                .checkout(LocalDateTime.of(2021, 7, 17, 10, 0))
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .roomsBooked(List.of(Room.builder()
                        .id(22L)
                        .capacity((short) 2)
                        .property(Property.FERRIER_MEDWAY)
                        .roomName("Bedroom3")
                        .roomType("Double")
                        .build()))
                .build());

        bookingRepository.save(Booking.builder()
                .bookingUid(UUID.randomUUID())
                .bookingAmount(new BigDecimal("73.00"))
                .customerId(50L)
                .hasAddons(false)
                .isPaid(false)
                .checkin(LocalDateTime.of(2021, 6, 12, 14, 0))
                .checkout(LocalDateTime.of(2021, 6, 17, 10, 0))
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .roomsBooked(List.of(Room.builder()
                        .id(22L)
                        .capacity((short) 2)
                        .property(Property.FERRIER_MEDWAY)
                        .roomName("Bedroom3")
                        .roomType("Double")
                        .build()))
                .build());
    }

    @Test
    void getBookingById_WillReturnBookingDtoWithExtras() throws JsonProcessingException {
        var extra = new Extras(List.of(
                new Addon(AddonCategory.BREAKFAST, AddonType.ORANGE_JUICE, BigDecimal.ONE, 1),
                new Addon(AddonCategory.BREAKFAST, AddonType.PAIN_AU_CHOCOLATE, BigDecimal.TEN, 2)),
                false);

        String s = objectMapper.writeValueAsString(extra);
        wireMockServer.stubFor(get(AddonServiceImpl.ADDON_PATH_V1 + testUuid)
                .willReturn(okJson(s)));

        BookingDto dto = bookingService.getBookingById(1L);

        assertThat(dto.getExtras().getAddonList()).hasSize(2);
    }
}