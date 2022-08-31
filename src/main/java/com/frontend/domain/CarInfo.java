package com.frontend.domain;

public final class CarInfo {

    private final String carName;
    private final String price;

    private CarInfo(String carName, String price) {
        this.carName = carName;
        this.price = price;
    }

    public static class CarInfoBuilder {

        private String carName;
        private String price;

        public CarInfoBuilder carName(String airline) {
            this.carName = airline;
            return this;
        }

        public CarInfoBuilder price(String price) {
            this.price = price;
            return this;
        }

        public CarInfo build() {
            return new CarInfo(carName, price);
        }
    }

    public String getCarName() {
        return carName;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "CarInfo{" +
                "airline='" + carName + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
