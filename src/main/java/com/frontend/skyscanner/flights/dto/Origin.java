package com.frontend.skyscanner.flights.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Origin {
    public String id;
    public String name;
    public String displayCode;
    public String city;
    public boolean isHighlighted;
    public String flightPlaceId;
    public Parent parent;
    public String type;
}
