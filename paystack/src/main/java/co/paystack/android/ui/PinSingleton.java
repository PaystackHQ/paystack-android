package co.paystack.android.ui;

public class PinSingleton
{
    private String pin = "";

    public String getPin() {
        return pin;
    }

    public PinSingleton setPin(String pin) {
        this.pin = pin;
        return this;
    }

    private PinSingleton() { }
    private static PinSingleton instance = new PinSingleton();

    public static PinSingleton getInstance() {
        return instance;
    }
}