package com.ecalm.e_calm.data.preferences;

import android.content.SharedPreferences;

public class UserPreferencesImpl implements UserPreferences {

    protected SharedPreferences mPreferences;

    public UserPreferencesImpl() {
        this.mPreferences = PreferencesProvider.providePreferences();
    }

    @Override
    public void setUserLogin(boolean status) {
        mPreferences.edit().putBoolean(IS_USER_LOGIN,status).apply();
    }

    @Override
    public boolean isUserLogin() {
        return mPreferences.getBoolean(IS_USER_LOGIN, false);
    }

    @Override
    public void clearUser() {
        mPreferences.edit()
                .putBoolean(IS_USER_LOGIN, false)
                .apply();
    }
}
