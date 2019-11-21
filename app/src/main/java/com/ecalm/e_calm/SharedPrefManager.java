package com.ecalm.e_calm;

import android.content.Context;
import android.content.SharedPreferences;

import com.ecalm.e_calm.data.models.UserModel;
import com.ecalm.e_calm.math.BMR;
import com.ecalm.e_calm.model.User;

public class SharedPrefManager {
    Context ctx;
    private static SharedPrefManager mInstance;

    public static final String SHARED_PREF_NAME = "fanregisterlogin";
    public static final String KEY_NAME = "keyname";
    public static final String KEY_WEIGHT = "keyweight";
    public static final String KEY_TALL = "keytall";
    public static final String KEY_GENDER = "keygender";
    public static final String KEY_AGE = "keyage";
    public static final String KEY_CALORIE = "keycalorie";
    public static final String KEY_HOBBY = "keyhobby";
    public static final String KEY_LIMIT = "keylimit";
    public static final String KEY_URL = "keyurl";

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void userRegister(User userModel) {
        BMR cal = new BMR();
        float tmp;
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, userModel.getName());
        editor.putString(KEY_GENDER, Integer.toString(userModel.getGender()));
        editor.putString(KEY_AGE, Integer.toString(userModel.getAge()));
        editor.putString(KEY_WEIGHT, Float.toString(userModel.getWeight()));
        editor.putString(KEY_TALL, Float.toString(userModel.getHeight()));
        editor.putString(KEY_HOBBY, "Swimming");
        editor.putString(KEY_URL, "http://192.168.1.2/");
        editor.putString(KEY_CALORIE, "0");
        if (userModel.getGender()==0){
            tmp = cal.getMaleBmr(userModel.getWeight(), userModel.getHeight(), userModel.getAge());
        }else if (userModel.getGender()==1){
            tmp = cal.getFemaleBmr(userModel.getWeight(), userModel.getHeight(), userModel.getAge());
        }else {
            tmp = 1000f;
        }
        editor.putString(KEY_LIMIT, Float.toString(tmp));
        editor.apply();
    }

    //this methode will checker user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null) != null;
    }


}
