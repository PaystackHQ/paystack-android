package co.paystack.android.utils;

import android.text.TextUtils;

import org.parceler.guava.primitives.Chars;

/**
 * String utility methods
 *
 * @author   {androidsupport@paystack.co} on 9/13/15.
 */
public class StringUtils {

    public static boolean isEmpty(String value){
        return value == null || value.length() < 1 || value.equalsIgnoreCase("null");
    }

    public static boolean isEmpty(CharSequence charSequence){
        return charSequence == null || isEmpty(charSequence.toString());
    }

    /**
     * Method to nullify a String.
     * @param value
     * @return null if a value is empty or null, otherwise, returns the value
     */
    public static String nullify(String value){
        if(isEmpty(value)){
            return null;
        }
        return value;
    }
}
