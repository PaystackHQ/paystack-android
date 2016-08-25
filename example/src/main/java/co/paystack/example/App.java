package co.paystack.example;

import android.app.Application;
import android.content.Context;

/**
 * The Example App
 * <p/>
 * This class allows us capture the application context and capture the app instance
 */
public class App extends Application {

    private static Context sContext;

    private static App sInstance;

    public static Context getAppContext() {
//        if(sContext == null){
//            return getApplica;
//        }

        return sContext;
    }

    private void setAppContext(Context context) {
        sContext = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        sContext = getApplicationContext();

        setAppContext(sContext);
    }
}
