package com.aledev.alba.msbnbbookingservice.web.model;

import com.aledev.alba.msbnbbookingservice.domain.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    private UUID bookingUid;
    private Long customerId;
    private LocalDateTime checkin;
    private LocalDateTime checkout;
    private BigDecimal bookingAmount;
    private Boolean isPaid;
    private Boolean hasAddons;
    private Extras extras;
    private Property property;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
}
