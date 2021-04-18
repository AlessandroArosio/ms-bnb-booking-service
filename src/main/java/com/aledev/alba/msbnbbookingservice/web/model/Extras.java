package com.aledev.alba.msbnbbookingservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Extras {
    private List<Addon> addonList;
    private Boolean isPaid;
}
