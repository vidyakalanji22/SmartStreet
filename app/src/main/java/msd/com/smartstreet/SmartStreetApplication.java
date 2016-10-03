
package msd.com.smartstreet;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.firebase.client.Firebase;

public class SmartStreetApplication extends android.app.Application {
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

