package com.frontend.skyscanner.flights.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Segment {
    public String id;
    public Origin origin;
    public Destination destination;
    public Date departure;
    public Date arrival;
    public int durationInMinutes;
    public String flightNumber;
    public MarketingCarrier marketingCarrier;
    public OperatingCarrier operatingCarrier;
}
