package test.example.internetmonthlyrentcharges;

import com.firebase.client.Firebase;

/**
 * Created by air on 03/10/16.
 */

public class InternetMonthlyRentCharges2Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // setting up global context for firebase database
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        // other setup code
    }
}
