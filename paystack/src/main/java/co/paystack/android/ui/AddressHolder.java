package co.paystack.android.ui;

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
    }

}