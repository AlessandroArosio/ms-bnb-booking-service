package com.aledev.alba.msbnbbookingservice.repository;

import com.aledev.alba.msbnbbookingservice.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
