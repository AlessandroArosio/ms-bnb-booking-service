package com.aledev.alba.msbnbbookingservice.web.model;

import java.math.BigDecimal;

public record Addon(AddonCategory category, AddonType type, BigDecimal price, Integer quantity) {}
