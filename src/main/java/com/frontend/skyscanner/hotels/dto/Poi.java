package com.frontend.skyscanner.hotels.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Poi{
    public ArrayList<Double> coordinate;
    public Object is_extend;
    public String name;
    public String image_url;
    public String sub_poi_type;
    public String type;
    public int id;
}
