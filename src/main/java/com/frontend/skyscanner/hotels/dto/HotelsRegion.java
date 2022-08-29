package com.frontend.skyscanner.hotels.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelsRegion{
    public double latitude;
    public double longitude;
    public double longitudeDelta;
    public double latitudeDelta;
}
