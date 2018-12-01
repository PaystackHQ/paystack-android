package co.paystack.android.api.print;

import android.content.Context;
import android.support.annotation.RequiresApi;

import co.paystack.android.PaystackSdk;
import co.paystack.android.model.Receipt;

/**
 * Created by Oluwagbenga on 29,November,2018
 */

public class PrintBuilder{

    private Context mContext;
    private PrinterTemplate printerTemp;



    public PrintBuilder(){

    }


    public Receipt getReceipt(){
        return new Receipt();
    }



}