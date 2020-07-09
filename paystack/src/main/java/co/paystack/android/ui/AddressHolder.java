package co.paystack.android.ui;

import java.util.HashMap;

public class AddressHolder {
    private static AddressHolder instance = new AddressHolder();
    private static Object lock = new Object();
    private Address address = null;

    private AddressHolder() {
    }

    public static AddressHolder getInstance() {
        return instance;
    }

    public static Object getLock() {
        return lock;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static class Address {
        private String state = "";
        private String zipCode = "";
        private String city = "";
        private String street = "";

        public static String FIELD_ADDRESS = "address";
        public static String FIELD_CITY = "city";
        public static String FIELD_ZIP_CODE = "zip_code";
        public static String FIELD_STATE = "state";

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "state='" + state + '\'' +
                    ", zipCode='" + zipCode + '\'' +
                    ", city='" + city + '\'' +
                    ", street='" + street + '\'' +
                    '}';
        }

        public HashMap<String, String> toHashMap() {
            HashMap<String, String> params = new HashMap<>();
            params.put(FIELD_STATE, this.state);
            params.put(FIELD_ZIP_CODE, this.zipCode);
            params.put(FIELD_CITY, this.city);
            params.put(FIELD_ADDRESS, this.street);
            return params;
        }
    }

}