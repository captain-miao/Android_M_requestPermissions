package com.example.captain_miao.permissions;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class GapApplication extends Application {

    private static final String TAG = "GapApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        //memory leak
        refWatcher = LeakCanary.install(this);
    }


    public static RefWatcher getRefWatcher(Context context) {
        GapApplication application = (GapApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;
}
