package com.frontend.client;

import com.frontend.domain.CarInfo;
import com.frontend.domain.Flights;
import com.frontend.domain.Hotels;
import com.frontend.dto.TripDto;
import com.frontend.skyscanner.flights.dto.Item;
import com.frontend.skyscanner.hotels.dto.Hotel;
import com.frontend.skyscanner.rental.car.dto.CarRentInfo;
import lombok.RequiredArgsConstructor;
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

    private final RestTemplate restTemplate;

    public Double getForecastAverageTemperature(String location) {
        String url = "http://localhost:8888/v1/weather/forecast/" + location + "/temperature/";
        return restTemplate.getForEntity(url, Double.class).getBody();
    }

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
        return flights;
    }

    public List<CarInfo> getCars(String pickupId, String pickUpdate, String returnDate) {
        String url = "http://localhost:8888/v1/rental/cars/searchAll/" + pickupId + "/" + pickUpdate  + "/10:00/" + returnDate +  "/10:00";
        ResponseEntity<CarRentInfo[]> response = restTemplate.getForEntity(url, CarRentInfo[].class);
        List<CarInfo> carInfo =  Arrays.stream(Objects.requireNonNull(response.getBody()))
                .map(info -> new CarInfo.CarInfoBuilder()
                        .carName(info.getCar_name())
                        .price(String.valueOf(info.getPrice()).split("\\.")[0] + " €")
                        .build())
                .collect(Collectors.toList());
        return carInfo;
    }

    public List<Hotels> getHotels(String locationId, int adultsNumber, int roomsNumber, String checkin, String checkout) {
        String url = "http://localhost:8888/v1/hotels/" + locationId + "/" + adultsNumber + "/" + roomsNumber + "/" + checkin + "/" + checkout;
        ResponseEntity<Hotel[]> response = restTemplate.getForEntity(url, Hotel[].class);
        List<Hotels> hotels = Objects.requireNonNull(Arrays.asList(response.getBody())).stream()
                .map(hotel -> new Hotels.HotelsBuilder()
                        .name(hotel.getName())
                        .price(hotel.getPrice())
                        .build())
                .collect(Collectors.toList());
        return hotels;
    }

    public List<Hotels> getHotelsUnderPrice(Integer maxPrice, String locationId, int adultsNumber, int roomsNumber, String checkin, String checkout) {
        String url = "http://localhost:8888/v1/hotels/searchByMaxPrice/" + maxPrice + "/" + locationId + "/" + adultsNumber + "/" + roomsNumber + "/" + checkin + "/" + checkout;
        ResponseEntity<Hotel[]> response = restTemplate.getForEntity(url, Hotel[].class);
        List<Hotels> hotels = Objects.requireNonNull(Arrays.asList(response.getBody())).stream()
                .map(hotel -> new Hotels.HotelsBuilder()
                        .name(hotel.getName())
                        .price(hotel.getPrice())
                        .build())
                .collect(Collectors.toList());
        return hotels;
    }

    public List<CarInfo> getCarsUnderPrice(Integer maxPrice, String pickupId, String pickUpdate, String returnDate) {
        String url = "http://localhost:8888/v1/hotels/searchByPrice/" + maxPrice + "/" + pickupId + "/" + pickUpdate + "/" + returnDate;
        ResponseEntity<CarRentInfo[]> response = restTemplate.getForEntity(url, CarRentInfo[].class);
        List<CarInfo> cars = Objects.requireNonNull(Arrays.asList(response.getBody())).stream()
                .map(info -> new CarInfo.CarInfoBuilder()
                        .carName(info.getCar_name())
                        .price(String.valueOf(info.getPrice()))
                        .build())
                .collect(Collectors.toList());
        return cars;
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
