package com.aledev.alba.msbnbbookingservice.web.model;

import com.aledev.alba.msbnbbookingservice.domain.enums.AddonCategory;
import com.aledev.alba.msbnbbookingservice.domain.enums.AddonType;

import java.math.BigDecimal;

public record Addon(AddonCategory category, AddonType type, BigDecimal price, Integer quantity) {}
