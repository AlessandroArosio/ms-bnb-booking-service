package com.aledev.alba.msbnbbookingservice.web.model;

import com.aledev.alba.msbnbbookingservice.domain.enums.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {
    private Long id;

    private String roomType;

    private String roomName;

    @Enumerated(EnumType.STRING)
    private Property property;

    private Short capacity;
}
