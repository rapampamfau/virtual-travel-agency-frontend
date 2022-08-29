package com.frontend.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TripDto {
    private Long id;
    private String fromPlace;
    private String destinationPlace;
    private LocalDate departureDate;
    private String airlineName;
    private String hotelName;
    private Double summaryPrice;
}
