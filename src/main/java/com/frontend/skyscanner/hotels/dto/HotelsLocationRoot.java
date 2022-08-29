package com.frontend.skyscanner.hotels.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HotelsLocationRoot {
    public String hierarchy;
    public String location;
    public String entity_name;
    public Highlight highlight;
    public String entity_id;
    @JsonProperty("class")
    public String myclass;
}
