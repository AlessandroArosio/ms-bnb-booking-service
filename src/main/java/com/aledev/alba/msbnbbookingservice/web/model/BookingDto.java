package com.aledev.alba.msbnbbookingservice.web.model;

import com.aledev.alba.msbnbbookingservice.domain.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {

    @Null
    private Long id;

    @Null
    private UUID bookingUid;

    @NotNull
    private Long customerId;

    @NotNull
    private LocalDateTime checkin;

    @NotNull
    private LocalDateTime checkout;

    @Positive
    private BigDecimal bookingAmount;
    private Boolean isPaid;
    private Boolean hasAddons;
    private Extras extras;
    private String notes;

    @NotNull
    private List<Room> roomsBooked;

    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
}
