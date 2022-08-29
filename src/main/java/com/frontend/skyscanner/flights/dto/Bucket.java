package com.frontend.skyscanner.flights.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Bucket {
    public String id;
    public String name;
    public ArrayList<Item> items;
}
