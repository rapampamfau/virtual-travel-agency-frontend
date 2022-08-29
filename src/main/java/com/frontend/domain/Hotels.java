package com.frontend.domain;

public final class Hotels {

    private final String name;
    private final String price;

    private Hotels(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public static class HotelsBuilder {

        private String name;
        private String price;

        public HotelsBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HotelsBuilder price(String price) {
            this.price = price;
            return this;
        }

        public Hotels build() {
            return new Hotels(name, price);
        }
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Hotels{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
