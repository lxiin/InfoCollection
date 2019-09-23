package com.hxgd.collection;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class App extends Application {




    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        if (BuildConfig.DEBUG){
            Stetho.initializeWithDefaults(this);
            Logger.addLogAdapter(new AndroidLogAdapter());
        }

    }


    public static Context getAppContext() {
        return context;
    }


}
