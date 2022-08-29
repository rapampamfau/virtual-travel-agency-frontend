package com.frontend.client;

import com.frontend.domain.Flights;
import com.frontend.domain.Hotels;
import com.frontend.dto.TripDto;
import com.frontend.skyscanner.flights.dto.Item;
import com.frontend.skyscanner.hotels.dto.HotelRoomsRoot;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VTAClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(VTAClient.class);
    private final RestTemplate restTemplate;

    public List<Flights> getBestFlights(int adultsNumber, String departure, String destination, String departureDate) {
        String url = "http://localhost:8888/v1/flights/searchBestFlights/" + adultsNumber + "/" + departure + "/" + destination + "/" + departureDate;
        return getFlights(url);
    }

    public List<Flights> getCheapestFlights(int adultsNumber, String departure, String destination, String departureDate) {
        String url = "http://localhost:8888/v1/flights/searchCheapestFlights/" + adultsNumber + "/" + departure + "/" + destination + "/" + departureDate;
        return getFlights(url);
    }

    public List<Flights> getFastestFlights(int adultsNumber, String departure, String destination, String departureDate) {
        String url = "http://localhost:8888/v1/flights/searchFastestFlights/" + adultsNumber + "/" + departure + "/" + destination + "/" + departureDate;
        return getFlights(url);
    }

    public List<Flights> getDirectFlights(int adultsNumber, String departure, String destination, String departureDate) {
        String url = "http://localhost:8888/v1/flights/searchDirectFlights/" + adultsNumber + "/" + departure + "/" + destination + "/" + departureDate;
        return getFlights(url);
    }

    private List<Flights> getFlights(String url) {
        ResponseEntity<Item[]> response = restTemplate.getForEntity(url, Item[].class);
        List<Flights> flights = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .map(item -> new Flights.FlightsBuilder()
                        .airline(item.getLegs().get(0).getCarriers().getMarketing().get(0).getName())
                        .price(item.getPrice().getFormatted())
                        .build())

                .collect(Collectors.toList());

//        System.out.println(flights);
//        System.out.println(response.getBody().toString());
        return flights;
    }

    public List<Hotels> getHotels(String locationId, int adultsNumber, int roomsNumber, String checkin, String checkout) {
        String url = "http://localhost:8888/v1/hotels/" + locationId + "/" + adultsNumber + "/" + roomsNumber + "/" + checkin + "/" + checkout;
        ResponseEntity<HotelRoomsRoot> response = restTemplate.getForEntity(url, HotelRoomsRoot.class);
        List<Hotels> hotels = Objects.requireNonNull(response.getBody()).getHotels().stream()
                .map(hotel -> new Hotels.HotelsBuilder()
                        .name(hotel.getName())
                        .price(hotel.getPrice())
                        .build())
                .collect(Collectors.toList());

        if (response.getBody().getContext().getCompletionPercentage() < 100) {
            LOGGER.info("Hotels fetch incomplete, another try after 15 seconds");
        } else {
            LOGGER.info("Hotels fetch complete");
        }

//        System.out.println(hotels);
//        System.out.println(response.getBody().toString());
        return hotels;
    }


    public void saveTrip(TripDto tripDto) {
        String url = "http://localhost:8888/v1/trips";
        restTemplate.postForEntity(url, tripDto, Void.class);
    }

    public List<TripDto> getAllTrips() {
        String url = "http://localhost:8888/v1/trips";
        ResponseEntity<TripDto[]> response = restTemplate.getForEntity(url, TripDto[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public void deleteTrip(Long id) {
        String url = "http://localhost:8888/v1/trips" + "/" + id;
        restTemplate.delete(url);
    }

    public void updateTrip(TripDto tripDto) {
        String url = "http://localhost:8888/v1/trips";
        restTemplate.put(url, tripDto, Void.class);
    }
}
