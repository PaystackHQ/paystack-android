package co.paystack.android.ui;

public class PinSingleton {
    private static PinSingleton instance = new PinSingleton();
    private String pin = "";

    private PinSingleton() {
    }

    public static PinSingleton getInstance() {
        return instance;
    }

    public String getPin() {
        return pin;
    }

    public PinSingleton setPin(String pin) {
        this.pin = pin;
        return this;
    }
}