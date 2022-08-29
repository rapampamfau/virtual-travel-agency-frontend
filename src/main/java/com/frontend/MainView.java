package com.frontend;

import com.frontend.client.VTAClient;
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
    private Editor<TripDto> editor = tripDtoGrid.getEditor();
    private Binder<TripDto> binder = new Binder<>(TripDto.class);

    private VTAClient vtaClient = new VTAClient(new RestTemplate());

    private Map<String, String> cityIata_code = Map.of("Berlin", "BER", "London", "LHR");
    private Map<String, String> cityLocationId = Map.of("Berlin", "27547053", "London", "27544008");
    Long tripToRemove = null;

    public MainView() {
        //Skyscanner
        Select<String> departure = new Select<>("London", "Berlin");
        departure.setLabel("Departure");
        Label arrow = new Label("-->");
        Select<String> destination = new Select<>("Berlin", "London");
        destination.setLabel("Destination");
        Select<String> flightType = new Select<>("Best", "Cheapest", "Fastest", "Direct");
        flightType.setLabel("Flight type");
        Select<Integer> adultsNumber = new Select<>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        adultsNumber.setLabel("Adults number");
        Select<Integer> roomsNumber = new Select<>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        roomsNumber.setLabel("Rooms number");
        DatePicker datePicker = new DatePicker("Departure date");
        DatePicker checkoutDate = new DatePicker("Checkout date");
        Button searchButton = new Button("Search");

        TextField pickedFlight = new TextField("Picked flight");
        TextField pickedFlightPrice = new TextField("Flight price");
        TextField pickedHotel = new TextField("Picked hotel");
        TextField pickedHotelPrice = new TextField("Hotel price");
        Button addTripButton = new Button("Add trip");
        Button deleteTripButton = new Button("Delete trip");

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
                    Double summaryPrice = Double.parseDouble(pickedFlightPrice.getValue().split(" ")[0]) + Double.parseDouble(pickedHotelPrice.getValue().split(" ")[0]);
                    TripDto tripDto = TripDto.builder()
                            .departureDate(datePicker.getValue())
                            .destinationPlace(destination.getValue())
                            .fromPlace(departure.getValue())
                            .airlineName(pickedFlight.getValue())
                            .hotelName(pickedHotel.getValue())
                            .summaryPrice(summaryPrice)
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

        searchButton.addClickListener(event -> {
            LocalDate date = datePicker.getValue();
            LocalDate dateOfCheckout = checkoutDate.getValue();
            String formattedDate = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
            String formattedCheckoutDate =  dateOfCheckout.getYear() + "-" + dateOfCheckout.getMonthValue() + "-" + dateOfCheckout.getDayOfMonth();
            if (flightType.getValue().equals("Best")) {
                flightsGrid.setItems(vtaClient.getBestFlights(adultsNumber.getValue(), cityIata_code.get(departure.getValue()), cityIata_code.get(destination.getValue()), formattedDate));
            } else if (flightType.getValue().equals("Cheapest")) {
                flightsGrid.setItems(vtaClient.getCheapestFlights(adultsNumber.getValue(), cityIata_code.get(departure.getValue()), cityIata_code.get(destination.getValue()), formattedDate));
            } else if (flightType.getValue().equals("Fastest")) {
                flightsGrid.setItems(vtaClient.getFastestFlights(adultsNumber.getValue(), cityIata_code.get(departure.getValue()), cityIata_code.get(destination.getValue()), formattedDate));
            } else if (flightType.getValue().equals("Direct")) {
                flightsGrid.setItems(vtaClient.getDirectFlights(adultsNumber.getValue(), cityIata_code.get(departure.getValue()), cityIata_code.get(destination.getValue()), formattedDate));
            }

            List<Hotels> hotels = vtaClient.getHotels(cityLocationId.get(destination.getValue()), adultsNumber.getValue(), roomsNumber.getValue(), formattedDate, formattedCheckoutDate);
            if (!hotels.isEmpty()) {
                hotelsGrid.setItems(hotels);
            } else {
                hotels = vtaClient.getHotels(cityLocationId.get(destination.getValue()), adultsNumber.getValue(), roomsNumber.getValue(), formattedDate, formattedCheckoutDate);
                hotelsGrid.setItems(hotels);
            }

        });

        addEditor();
        flightsGrid.setWidth("500px");
        hotelsGrid.setWidth("500px");
        HorizontalLayout selects = new HorizontalLayout(departure, arrow, destination, flightType, adultsNumber, roomsNumber);
        HorizontalLayout grids = new HorizontalLayout(flightsGrid, hotelsGrid);
        HorizontalLayout summary = new HorizontalLayout(pickedFlight, pickedFlightPrice, pickedHotel, pickedHotelPrice);
        HorizontalLayout operationButtons = new HorizontalLayout(addTripButton, deleteTripButton);
        add(selects, datePicker, checkoutDate, searchButton, grids, summary,operationButtons, tripDtoGrid);
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
        tripDtoGrid.getColumnByKey("airlineName").setEditorComponent(airlineName);

        DatePicker departureDate = new DatePicker();
        departureDate.setWidthFull();
        binder.forField(departureDate)
                .bind(TripDto::getDepartureDate, TripDto::setDepartureDate);
        tripDtoGrid.getColumnByKey("departureDate").setEditorComponent(departureDate);

        TextField destinationPlace = new TextField();
        destinationPlace.setWidthFull();
        binder.forField(destinationPlace)
                .bind(TripDto::getDestinationPlace, TripDto::setDestinationPlace);
        tripDtoGrid.getColumnByKey("destinationPlace").setEditorComponent(destinationPlace);

        TextField fromPlace = new TextField();
        fromPlace.setWidthFull();
        binder.forField(fromPlace)
                .bind(TripDto::getFromPlace, TripDto::setFromPlace);
        tripDtoGrid.getColumnByKey("fromPlace").setEditorComponent(fromPlace);

        TextField hotelName = new TextField();
        hotelName.setWidthFull();
        binder.forField(hotelName)
                .bind(TripDto::getHotelName, TripDto::setHotelName);
        tripDtoGrid.getColumnByKey("hotelName").setEditorComponent(hotelName);
    }
}
