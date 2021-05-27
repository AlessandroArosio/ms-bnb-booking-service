package com.aledev.alba.msbnbbookingservice.web.controller.v1;

import com.aledev.alba.msbnbbookingservice.repository.RoomRepository;
import com.aledev.alba.msbnbbookingservice.web.mappers.RoomMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomRepository repository;
    private final RoomMapper mapper;

    @GetMapping
    @Operation(summary = "Fetch all the rooms in the BnB")
    public List<Object> getRooms() {
        return repository.findAll().stream()
                .map(mapper::roomToDto)
                .collect(Collectors.toList());
    }
}
