package com.frontend.skyscanner.hotels.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelRoomsRoot {
    @JsonProperty("hotels")
    public ArrayList<Hotel> hotels;

    @JsonProperty("context")
    public Context context;
}
