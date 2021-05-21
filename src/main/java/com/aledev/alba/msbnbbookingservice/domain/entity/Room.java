package com.aledev.alba.msbnbbookingservice.domain.entity;

import com.aledev.alba.msbnbbookingservice.domain.enums.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    private String roomType;

    @NotBlank
    @NotNull
    private String roomName;

    @Enumerated(EnumType.STRING)
    private Property property;

    @Min(1)
    @NotNull
    private Short capacity;

    @ManyToMany(mappedBy = "roomsBooked")
    List<Booking> bookings = new ArrayList<>();

    @Version
    private Long version;
}
