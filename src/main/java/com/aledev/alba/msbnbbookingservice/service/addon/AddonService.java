package com.aledev.alba.msbnbbookingservice.service.addon;

import com.aledev.alba.msbnbbookingservice.web.model.Extras;

import java.util.Optional;
import java.util.UUID;

public interface AddonService {
    Optional<Extras> getAllAddonsForBookingUUID(UUID uuid);
}
