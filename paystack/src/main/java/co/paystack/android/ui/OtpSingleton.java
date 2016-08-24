package co.paystack.android.ui;

public class OtpSingleton {
    private String otp = "";
    private String otpMessage = "";

    public String getOtpMessage() {
        return otpMessage;
    }

    public OtpSingleton setOtpMessage(String otpMessage) {
        this.otpMessage = otpMessage;
        return this;
    }

    public String getOtp() {
        return otp;
    }

    public OtpSingleton setOtp(String otp) {
        this.otp = otp;
        return this;
    }

    private OtpSingleton() {
    }

    private static OtpSingleton instance = new OtpSingleton();

    public static OtpSingleton getInstance() {
        return instance;
    }
}