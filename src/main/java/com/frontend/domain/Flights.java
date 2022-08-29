package com.frontend.domain;

public final class Flights {

    private final String airline;
    private final String price;

    private Flights(String airline, String price) {
        this.airline = airline;
        this.price = price;
    }

    public static class FlightsBuilder {

        private String airline;
        private String price;

        public FlightsBuilder airline(String airline) {
            this.airline = airline;
            return this;
        }

        public FlightsBuilder price(String price) {
            this.price = price;
            return this;
        }

        public Flights build() {
            return new Flights(airline, price);
        }
    }

    public String getAirline() {
        return airline;
    }

    public String getPrice() {
        return price;
    }



    @Override
    public String toString() {
        return "FlightsRoot{" +
                "airline='" + airline + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
