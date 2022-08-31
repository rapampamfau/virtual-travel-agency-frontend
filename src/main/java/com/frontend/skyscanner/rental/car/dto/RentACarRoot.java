package com.frontend.skyscanner.rental.car.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RentACarRoot {
        public ArrayList<CarRentInfo> quotes;
        public int quotes_count;
        public Context context;
}
