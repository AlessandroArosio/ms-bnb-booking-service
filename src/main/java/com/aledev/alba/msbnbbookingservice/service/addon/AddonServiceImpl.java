package com.aledev.alba.msbnbbookingservice.service.addon;

import com.aledev.alba.msbnbbookingservice.web.model.Extras;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@ConfigurationProperties(prefix = "alba.bnb", ignoreUnknownFields = false)
public class AddonServiceImpl implements AddonService {
    public static final String ADDON_PATH_V1 = "/api/v1/addonOrder/extras/";
    private final RestTemplate restTemplate;

    private String addonServiceHost;

    public AddonServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Optional<Extras> getAllAddonsForBookingUUID(UUID uuid) {
        return Optional.of(restTemplate.getForObject(addonServiceHost + ADDON_PATH_V1 + uuid.toString(), Extras.class));
    }

    public void setAddonServiceHost(String addonServiceHost) {
        this.addonServiceHost = addonServiceHost;
    }
}
