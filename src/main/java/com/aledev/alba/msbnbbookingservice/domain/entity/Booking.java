package com.aledev.alba.msbnbbookingservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID bookingUid;

    private Long customerId;

    @NotNull
    private LocalDateTime checkin;

    @NotNull
    private LocalDateTime checkout;

    @Positive
    private BigDecimal bookingAmount;
    private Boolean isPaid = false;
    private Boolean hasAddons = false;

    @OneToMany(targetEntity = Room.class, fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "roomId", referencedColumnName = "id")
    List<Room> roomsBooked = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @Version
    private Long version;
}
