package co.paystack.android.model;

import java.io.Serializable;

/**
 * The class for Token model.
 *
 * @author {androidsupport@paystack.co} on 8/10/15.
 */
public class Token extends PaystackModel implements Serializable {

    public String token;

    public String last4;


//    private Token(Parcel in){
//        String[] data = new String[2];
//
//        in.readStringArray(data);
//        token = data[0];
//        last4 = data[1];
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//
//    }
}
