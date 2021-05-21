package com.aledev.alba.msbnbbookingservice.web.controller.v1;

import com.aledev.alba.msbnbbookingservice.bootstrap.Bootstrap;
import com.aledev.alba.msbnbbookingservice.domain.entity.Booking;
import com.aledev.alba.msbnbbookingservice.domain.entity.Room;
import com.aledev.alba.msbnbbookingservice.domain.enums.AddonCategory;
import com.aledev.alba.msbnbbookingservice.domain.enums.AddonType;
import com.aledev.alba.msbnbbookingservice.domain.enums.Property;
import com.aledev.alba.msbnbbookingservice.domain.exceptions.BookingException;
import com.aledev.alba.msbnbbookingservice.repository.BookingRepository;
import com.aledev.alba.msbnbbookingservice.repository.RoomRepository;
import com.aledev.alba.msbnbbookingservice.service.BookingService;
import com.aledev.alba.msbnbbookingservice.service.addon.AddonServiceImpl;
import com.aledev.alba.msbnbbookingservice.web.model.Addon;
import com.aledev.alba.msbnbbookingservice.web.model.BookingDto;
import com.aledev.alba.msbnbbookingservice.web.model.Extras;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingControllerIT {

    @MockBean
    Bootstrap bootstrap;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    BookingController bookingController;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    ObjectMapper objectMapper;

    final static String testUuid = "7642c92b-4096-4515-a308-56f09ae30ed8";
    final static String API_ROOT = "/api/v1/bookings/";

    @TestConfiguration
    static class RestTemplateBuilderProvider {

        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer() {
            WireMockServer server = with(wireMockConfig().port(8090));
            server.start();
            return server;
        }
    }

    @BeforeAll
    static void beforeAll(@Autowired BookingService bookingService,
                          @Autowired RoomRepository roomRepository) {
        Room room = roomRepository.save(Room.builder()
                .capacity((short) 2)
                .property(Property.FERRIER_MEDWAY)
                .roomName("Bedroom3")
                .roomType("Double")
                .build());

        bookingService.newBooking(BookingDto.builder()
                .bookingAmount(new BigDecimal("45.00"))
                .customerId(50L)
                .hasAddons(true)
                .isPaid(false)
                .checkin(LocalDateTime.of(2021, 7, 15, 14, 0))
                .checkout(LocalDateTime.of(2021, 7, 17, 10, 0))
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .roomsBooked(List.of(room))
                .build());

        bookingService.newBooking(BookingDto.builder()
                .bookingAmount(new BigDecimal("73.00"))
                .customerId(50L)
                .hasAddons(false)
                .isPaid(false)
                .checkin(LocalDateTime.of(2021, 6, 12, 14, 0))
                .checkout(LocalDateTime.of(2021, 6, 17, 10, 0))
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .roomsBooked(List.of(room))
                .build());
    }

    @Order(1)
    @Test
    void testListBookings_ReturnsListOfAvailableBookings() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(API_ROOT))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].customerId", equalTo(50)))
                .andExpect(jsonPath("$.content[1].roomsBooked[0].roomType", equalTo("Double")));
    }

    @Order(2)
    @Test
    void testPlaceBooking_ReturnsStatusIsCreated() throws Exception {
        var dto = BookingDto.builder()
                .customerId(77L)
                .bookingAmount(new BigDecimal("107.60"))
                .checkin(LocalDateTime.now())
                .checkout(LocalDateTime.now().plusDays(4))
                .roomsBooked(List.of(roomRepository.findById(1L).get()))
                .build();

        String body = objectMapper.writeValueAsString(dto);

        mvc.perform(
                MockMvcRequestBuilders.post(API_ROOT)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.bookingAmount", equalTo(107.60)));
    }

    @Order(3)
    @Test
    void testGetBookingById_WillReturnBookingDtoWithExtras() throws Exception {
        var uuid = bookingRepository.findById(1L).map(Booking::getBookingUid).orElseThrow();
        var extra = new Extras(List.of(
                new Addon(AddonCategory.BREAKFAST, AddonType.ORANGE_JUICE, BigDecimal.ONE, 1),
                new Addon(AddonCategory.BREAKFAST, AddonType.PAIN_AU_CHOCOLATE, BigDecimal.TEN, 2)),
                false);

        String s = objectMapper.writeValueAsString(extra);
        wireMockServer.stubFor(get(AddonServiceImpl.ADDON_PATH_V1 + uuid)
                .willReturn(okJson(s)));

        mvc.perform(MockMvcRequestBuilders.get(API_ROOT + "/1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.extras.addonList[0].category",
                        equalTo(AddonCategory.BREAKFAST.name())));
    }

    @Order(4)
    @Test
    void testGetBookingByUuId_ReturnsBooking() throws Exception {
        var uid = bookingRepository.findById(1L).map(Booking::getBookingUid).orElseThrow();
        var extra = new Extras(List.of(
                new Addon(AddonCategory.BREAKFAST, AddonType.ORANGE_JUICE, BigDecimal.ONE, 1),
                new Addon(AddonCategory.BREAKFAST, AddonType.PAIN_AU_CHOCOLATE, BigDecimal.TEN, 2)),
                false);

        String s = objectMapper.writeValueAsString(extra);
        wireMockServer.stubFor(get(AddonServiceImpl.ADDON_PATH_V1 + uid)
                .willReturn(okJson(s)));

        mvc.perform(MockMvcRequestBuilders.get(API_ROOT + "/uuid/" + uid))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingAmount", equalTo(45.0)));
    }

    @Order(5)
    @Test
    void testUpdateBooking_ReturnsAmendedBooking() throws Exception {
        var dto = BookingDto.builder()
                .customerId(77L)
                .bookingAmount(new BigDecimal("107.60"))
                .checkin(LocalDateTime.now())
                .checkout(LocalDateTime.now().plusDays(9))
                .roomsBooked(List.of(roomRepository.findById(1L).get()))
                .build();

        String body = objectMapper.writeValueAsString(dto);

        mvc.perform(
                MockMvcRequestBuilders.put(API_ROOT + "/3")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roomsBooked[0].roomType", equalTo("Double")));
    }

    @Order(6)
    @Test
    void testDeleteBooking_ReturnsStatusNoContent() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(API_ROOT + "/3"))
                .andExpect(status().isNoContent());
    }

    @Order(7)
    @Test
    void testPlaceBooking_WithInvalidDto_ThrowsException() throws Exception {
        var dto = BookingDto.builder()
                .customerId(77L)
                .checkin(LocalDateTime.now())
                .checkout(LocalDateTime.now().plusDays(4))
                .build();

        String body = objectMapper.writeValueAsString(dto);

        mvc.perform(
                MockMvcRequestBuilders.post(API_ROOT)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isNotNull());
    }

    @Order(8)
    @Test
    void testGetBookingById_ThrowsExceptionBookingNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(API_ROOT + "/165"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookingException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .isEqualTo("Booking ID 165: not found"));;
    }
}
