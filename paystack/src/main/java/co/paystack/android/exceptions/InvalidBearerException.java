package co.paystack.android.exceptions;

/**
 * Created by i on 24/08/2016.
 */
public class InvalidBearerException extends PaystackException {

    private String bearer;

    public InvalidBearerException(String bearer) {
        super(bearer + " is not a valid bearer.");
        this.setBearer(bearer);
    }

    public String getBearer() {
        return bearer;
    }

    public InvalidBearerException setBearer(String bearer) {
        this.bearer = bearer;
        return this;
    }

}
