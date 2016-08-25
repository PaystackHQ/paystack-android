package co.paystack.android.ui;

public class OtpSingleton {
    private static OtpSingleton instance = new OtpSingleton();
    private String otp = "";
    private String otpMessage = "";

    private OtpSingleton() {
    }

    public static OtpSingleton getInstance() {
        return instance;
    }

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
}