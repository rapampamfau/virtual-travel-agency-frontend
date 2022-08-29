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
public class Hotel {
    public String hotelId;
    public String name;
    public int stars;
    public String distance;
    public Object relevantPoiDistance;
    public ArrayList<Double> coordinates;
    public String price;
    public String cheapestOfferPartnerId;
    public int rawPrice;
    public String cheapestOffer;
    public String offerTypes;
    public String pricesFrom;
    public String priceDescription;
    public String taxPolicy;
    public String cheapestOfferPartnerName;
}
