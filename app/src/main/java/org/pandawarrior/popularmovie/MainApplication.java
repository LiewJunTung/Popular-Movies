package org.pandawarrior.popularmovie;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Popular Movie App
 * Created by jtlie on 8/18/2016.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
