package com.frontend;

import com.frontend.client.VTAClient;
import com.frontend.domain.CarInfo;
import com.frontend.domain.Flights;
import com.frontend.domain.Hotels;
import com.frontend.dto.TripDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Route
public class MainView extends VerticalLayout {

    private Grid<Flights> flightsGrid = new Grid<>(Flights.class);
    private Grid<Hotels> hotelsGrid = new Grid<>(Hotels.class);
    private Grid<TripDto> tripDtoGrid = new Grid<>(TripDto.class);
    private Grid<CarInfo> carGrid = new Grid<>(CarInfo.class);
    private Editor<TripDto> editor = tripDtoGrid.getEditor();
    private Binder<TripDto> binder = new Binder<>(TripDto.class);
    IntegerField maxHotelPrice = new IntegerField("Max hotel price");
    Select<String> departure = new Select<>("London", "Berlin", "Rome", "Krakow", "Warsaw");
    Select<String> destination = new Select<>("Berlin", "London", "Warsaw", "Krakow", "Rome");
    Select<String> flightType = new Select<>("Best", "Cheapest", "Fastest", "Direct");
    Select<Integer> adultsNumber = new Select<>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    Select<Integer> roomsNumber = new Select<>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    DatePicker datePicker = new DatePicker("Departure date");
    DatePicker checkoutDate = new DatePicker("Checkout date");
    Button searchButton = new Button("Search");
    Label label = new Label("For complete search result wait a second");

    TextField pickedFlight = new TextField("Picked flight");
    TextField pickedFlightPrice = new TextField("Flight price");
    TextField pickedHotel = new TextField("Picked hotel");
    TextField pickedHotelPrice = new TextField("Hotel price");
    TextField pickedCar= new TextField("Picked car");
    TextField pickedCarPrice = new TextField("Car price");
    Button addTripButton = new Button("Add trip");
    Button deleteTripButton = new Button("Delete trip");
    IntegerField maxCarRentPrice = new IntegerField("Max car rent price");


    private VTAClient vtaClient = new VTAClient(new RestTemplate());

    private Map<String, String> cityIata_code = Map.of(
            "Berlin", "BER",
            "London", "LHR",
            "Warsaw", "WAW",
            "Krakow", "KRK",
            "Rome", "ROME"
            );
    private Map<String, String> cityLocationId = Map.of(
            "Berlin", "27547053",
            "London", "27544008",
            "Warsaw", "95673438",
            "Krakow", "27543787",
            "Rome", "27539793"
            );
    Long tripToRemove = null;

    public MainView() {
        departure.setLabel("Departure");
        destination.setLabel("Destination");
        flightType.setLabel("Flight type");
        adultsNumber.setLabel("Adults number");
        roomsNumber.setLabel("Rooms number");

        tripDtoGrid.setItems(vtaClient.getAllTrips());

        tripDtoGrid.addItemClickListener(item -> {
            tripToRemove = item.getItem().getId();
        });

        deleteTripButton.addClickListener(
                click -> {
                    vtaClient.deleteTrip(tripToRemove);
                    tripDtoGrid.setItems(vtaClient.getAllTrips());
                    tripToRemove = null;
                });

        addTripButton.addClickListener(
                click -> {
                    Double summaryPrice = Double.parseDouble(pickedFlightPrice.getValue().split(" ")[0]) + Double.parseDouble(pickedHotelPrice.getValue().split(" ")[0]) +  Double.parseDouble(pickedCarPrice.getValue().split(" ")[0]);
                    TripDto tripDto = TripDto.builder()
                            .departureDate(datePicker.getValue())
                            .destinationPlace(destination.getValue())
                            .fromPlace(departure.getValue())
                            .airlineName(pickedFlight.getValue())
                            .hotelName(pickedHotel.getValue())
                            .summaryPrice(summaryPrice)
                            .carName(pickedCar.getValue())
                            .forecastedAverageTemperature(String.valueOf(vtaClient.getForecastAverageTemperature(destination.getValue())).split("\\.")[0]  + "°C")
                            .build();
                    vtaClient.saveTrip(tripDto);
                    tripDtoGrid.setItems(vtaClient.getAllTrips());
                });

        flightsGrid.addItemClickListener(item -> {
            pickedFlight.setValue(item.getItem().getAirline());
            pickedFlightPrice.setValue(item.getItem().getPrice());
        });

        hotelsGrid.addItemClickListener(item -> {
            pickedHotel.setValue(item.getItem().getName());
            pickedHotelPrice.setValue(item.getItem().getPrice());
        });

        carGrid.addItemClickListener(item -> {
            pickedCar.setValue(item.getItem().getCarName());
            pickedCarPrice.setValue(String.valueOf(item.getItem().getPrice()));
        });

        searchButton.addClickListener(event -> {
            LocalDate date = datePicker.getValue();
            LocalDate dateOfCheckout = checkoutDate.getValue();
            String formattedDate = formatDateToString(date);
            String formattedCheckoutDate =  formatDateToString(dateOfCheckout);

            if (flightType.getValue().equals("Best")) {
                flightsGrid.setItems(vtaClient.getBestFlights(adultsNumber.getValue(), cityIata_code.get(departure.getValue()), cityIata_code.get(destination.getValue()), formattedDate));
            } else if (flightType.getValue().equals("Cheapest")) {
                flightsGrid.setItems(vtaClient.getCheapestFlights(adultsNumber.getValue(), cityIata_code.get(departure.getValue()), cityIata_code.get(destination.getValue()), formattedDate));
            } else if (flightType.getValue().equals("Fastest")) {
                flightsGrid.setItems(vtaClient.getFastestFlights(adultsNumber.getValue(), cityIata_code.get(departure.getValue()), cityIata_code.get(destination.getValue()), formattedDate));
            } else if (flightType.getValue().equals("Direct")) {
                flightsGrid.setItems(vtaClient.getDirectFlights(adultsNumber.getValue(), cityIata_code.get(departure.getValue()), cityIata_code.get(destination.getValue()), formattedDate));
            }

            List<Hotels> hotels;
            hotels = setHotels(formattedDate, formattedCheckoutDate);
            if (!hotels.isEmpty()) {
                hotelsGrid.setItems(hotels);
            } else {
                hotels = setHotels(formattedDate, formattedCheckoutDate);
                hotelsGrid.setItems(hotels);
            }

            List<CarInfo> cars;
            cars = setCars(formattedDate, formattedCheckoutDate);
            if (!cars.isEmpty()) {
                carGrid.setItems(cars);
            } else {
                cars = setCars(formattedDate, formattedCheckoutDate);
                carGrid.setItems(cars);
            }
        });

        addEditor();

        flightsGrid.setWidth("500px");
        hotelsGrid.setWidth("500px");
        carGrid.setWidth("500px");
        HorizontalLayout selects = new HorizontalLayout(departure, destination, flightType, adultsNumber, new VerticalLayout(roomsNumber, maxHotelPrice));
        HorizontalLayout grids = new HorizontalLayout(flightsGrid, hotelsGrid, carGrid);
        HorizontalLayout summary = new HorizontalLayout(pickedFlight, pickedFlightPrice, pickedHotel, pickedHotelPrice, pickedCar, pickedCarPrice);
        HorizontalLayout operationButtons = new HorizontalLayout(addTripButton, deleteTripButton);
        add(selects, datePicker, checkoutDate, new HorizontalLayout(searchButton, label), grids, summary,operationButtons, tripDtoGrid);
    }

    public String formatDateToString(LocalDate date) {
        if (date.getMonthValue() < 10 && date.getDayOfMonth() < 10) {
            return date.getYear() + "-0" + date.getMonthValue() + "-0" + date.getDayOfMonth();
        } else if (date.getMonthValue() < 10) {
            return date.getYear() + "-0" + date.getMonthValue() + "-" + date.getDayOfMonth();
        } else if (date.getDayOfMonth() < 10) {
            return date.getYear() + "-" + date.getMonthValue() + "-0" + date.getDayOfMonth();
        } else {
            return date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
        }
    }

    public List<Hotels> setHotels(String formattedDate, String formattedCheckoutDate) {
        List<Hotels> hotels;
        if (maxHotelPrice.getValue() != null) {
            hotels = vtaClient.getHotelsUnderPrice(maxHotelPrice.getValue(), cityLocationId.get(destination.getValue()), adultsNumber.getValue(), roomsNumber.getValue(), formattedDate, formattedCheckoutDate);
        } else {
            hotels = vtaClient.getHotels(cityLocationId.get(destination.getValue()), adultsNumber.getValue(), roomsNumber.getValue(), formattedDate, formattedCheckoutDate);
        }
        return hotels;
    }

    public List<CarInfo> setCars(String formattedDate, String formattedCheckoutDate) {
        List<CarInfo> cars;
        if (maxCarRentPrice.getValue() != null) {
            cars = vtaClient.getCarsUnderPrice(maxCarRentPrice.getValue(), cityLocationId.get(destination.getValue()), formattedDate, formattedCheckoutDate);
        } else {
            cars = vtaClient.getCars(cityLocationId.get(destination.getValue()), formattedDate, formattedCheckoutDate);
        }
        return cars;
    }

    public void addEditor() {
        Grid.Column<TripDto> editColumn = tripDtoGrid.addComponentColumn(column -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(clickEvent -> {
                if (editor.isOpen())
                    editor.cancel();
                tripDtoGrid.getEditor().editItem(column);
            });
            return editButton;
        }).setWidth("150x").setFlexGrow(0);
        editor.setBinder(binder);
        editor.setBuffered(true);
        Button saveButton = new Button("Save", e -> {
            editor.save();
        });
        editor.addSaveListener(x -> {
            vtaClient.updateTrip(editor.getItem());
        });
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton,
                cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);

        TextField airlineName = new TextField();
        airlineName.setWidthFull();
        binder.forField(airlineName)
                .bind(TripDto::getAirlineName, TripDto::setAirlineName);
        tripDtoGrid.getColumnByKey("airlineName").setEditorComponent(airlineName).setAutoWidth(true);

        DatePicker departureDate = new DatePicker();
        departureDate.setWidthFull();
        binder.forField(departureDate)
                .bind(TripDto::getDepartureDate, TripDto::setDepartureDate);
        tripDtoGrid.getColumnByKey("departureDate").setEditorComponent(departureDate).setAutoWidth(true);

        TextField destinationPlace = new TextField();
        destinationPlace.setWidthFull();
        binder.forField(destinationPlace)
                .bind(TripDto::getDestinationPlace, TripDto::setDestinationPlace);
        tripDtoGrid.getColumnByKey("destinationPlace").setEditorComponent(destinationPlace).setAutoWidth(true);

        TextField fromPlace = new TextField();
        fromPlace.setWidthFull();
        binder.forField(fromPlace)
                .bind(TripDto::getFromPlace, TripDto::setFromPlace);
        tripDtoGrid.getColumnByKey("fromPlace").setEditorComponent(fromPlace).setAutoWidth(true);

        TextField hotelName = new TextField();
        hotelName.setWidthFull();
        binder.forField(hotelName)
                .bind(TripDto::getHotelName, TripDto::setHotelName);
        tripDtoGrid.getColumnByKey("hotelName").setEditorComponent(hotelName).setAutoWidth(true);

        TextField carName = new TextField();
        carName.setWidthFull();
        binder.forField(carName)
                .bind(TripDto::getCarName, TripDto::setCarName);
        tripDtoGrid.getColumnByKey("carName").setEditorComponent(carName).setAutoWidth(true);
    }
}
