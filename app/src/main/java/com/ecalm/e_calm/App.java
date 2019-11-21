package com.ecalm.e_calm;

import android.app.Application;

import com.ecalm.e_calm.data.preferences.PreferencesProvider;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesProvider.init(this);
    }
}
