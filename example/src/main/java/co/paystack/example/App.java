package co.paystack.example;

import android.app.Application;
import android.content.Context;

/**
 * Created by Segun Famisa {segunfamisa@gmail.com} on 9/22/15.
 */
public class App extends Application {

    private static Context sContext;

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        sContext = getApplicationContext();

        setAppContext(sContext);
    }

    public static Context getAppContext(){
//        if(sContext == null){
//            return getApplica;
//        }

        return sContext;
    }

    private void setAppContext(Context context){
        sContext = context;
    }
}
