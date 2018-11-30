package co.paystack.android.api.print;

import android.content.Context;
import android.support.annotation.RequiresApi;

import co.paystack.android.model.Receipt;

/**
 * Created by Oluwagbenga on 29,November,2018
 */

public class PrintBuilder{

    private Context mContext;
    private PrinterTemplate printerTemp;

    public Receipt getInstance(Context context){
        return new PrintBuilder().setContext(context).getReceipt();
    }

    public PrintBuilder setContext(Context context){
        this.mContext =context;
        return this;
    }

    public Receipt getReceipt(){
        return new Receipt();
    }

    public PrinterTemplate create(Context mContext){
        return new PrinterTemplate(mContext);
    }


}