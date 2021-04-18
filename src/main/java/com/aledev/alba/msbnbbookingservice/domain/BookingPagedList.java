package com.aledev.alba.msbnbbookingservice.domain;

import com.aledev.alba.msbnbbookingservice.web.model.BookingDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BookingPagedList extends PageImpl<BookingDto> {
    public BookingPagedList(List<BookingDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public BookingPagedList(List<BookingDto> content) {
        super(content);
    }
}
