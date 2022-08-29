package com.frontend.skyscanner.flights.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarketingCarrier {
    public int id;
    public String name;
    public String alternateId;
    public int allianceId;
}
