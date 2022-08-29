package com.frontend.skyscanner.hotels.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Context{
    public int completionPercentage;
    public String status;
    public String searchId;
}
