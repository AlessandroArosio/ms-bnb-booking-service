package com.aledev.alba.msbnbbookingservice.web.mappers;

import com.aledev.alba.msbnbbookingservice.domain.entity.Room;
import com.aledev.alba.msbnbbookingservice.web.model.RoomDto;
import org.mapstruct.Mapper;

@Mapper
public interface RoomMapper {
    RoomDto roomToDto(Room room);
    Room dtoToRoom(RoomDto dto);
}
