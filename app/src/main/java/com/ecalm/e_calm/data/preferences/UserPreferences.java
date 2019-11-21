package com.ecalm.e_calm.data.preferences;

public interface UserPreferences {
    String IS_USER_LOGIN = "isUserLogin";
    public void setUserLogin(boolean status);
    public boolean isUserLogin();
    public void clearUser();
}
