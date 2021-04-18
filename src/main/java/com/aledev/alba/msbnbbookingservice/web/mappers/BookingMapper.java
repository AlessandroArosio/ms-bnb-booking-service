package com.aledev.alba.msbnbbookingservice.web.mappers;

import com.aledev.alba.msbnbbookingservice.domain.Booking;
import com.aledev.alba.msbnbbookingservice.web.model.BookingDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BookingMapper {

    BookingDto bookingToDto(Booking booking);
    Booking dtoToBooking(BookingDto dto);
}
