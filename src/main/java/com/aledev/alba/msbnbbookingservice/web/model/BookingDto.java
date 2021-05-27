package com.aledev.alba.msbnbbookingservice.web.model;

import com.aledev.alba.msbnbbookingservice.domain.entity.Room;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {

    private Long id;

    private UUID bookingUid;

    @NotNull
    private Long customerId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private LocalDate checkin;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private LocalDate checkout;

    @Positive
    private BigDecimal bookingAmount;
    private Boolean isPaid;
    private Boolean hasAddons;
    private Extras extras;
    private String notes;

    @NotNull
    private String confirmationCode;

    @NotNull
    private List<Room> roomsBooked;

    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
}
