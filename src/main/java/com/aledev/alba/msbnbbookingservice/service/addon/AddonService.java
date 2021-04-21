package com.aledev.alba.msbnbbookingservice.service.addon;

import com.aledev.alba.msbnbbookingservice.web.model.Extras;

import java.util.UUID;

public interface AddonService {
    Extras getAllAddonsForBookingUUID(UUID uuid);
}
