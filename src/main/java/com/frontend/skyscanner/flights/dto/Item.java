package com.frontend.skyscanner.flights.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {
    public String id;
    public Price price;
    public ArrayList<Leg> legs;
    public boolean isSelfTransfer;
    public ArrayList<String> tags;
    public boolean isMashUp;
    public boolean hasFlexibleOptions;
    public double score;
    public String deeplink;
    public Eco eco;
}
