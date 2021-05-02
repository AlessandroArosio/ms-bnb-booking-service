package com.aledev.alba.msbnbbookingservice.web.model;

import com.aledev.alba.msbnbbookingservice.domain.enums.AddonCategory;
import com.aledev.alba.msbnbbookingservice.domain.enums.AddonType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

@JsonSerialize
public record Addon(AddonCategory category, AddonType type, BigDecimal price, Integer quantity) {}
