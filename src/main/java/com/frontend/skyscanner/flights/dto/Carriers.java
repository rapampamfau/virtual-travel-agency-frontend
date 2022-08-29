package com.frontend.skyscanner.flights.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Carriers {
    public ArrayList<Marketing> marketing;
    public String operationType;
    public ArrayList<Operating> operating;
}
