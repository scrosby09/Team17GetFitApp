package com.team17.team17getfitapp;

import com.team17.team17getfitapp.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;

import java.util.HashMap;

public class SessionManager {

    // All Shared Preferences Keys
    public static final String PREF_NAME = "GetFitAppPreferences";
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    public static int PRIVATE_MODE = 0;
    private HashMap<String, String> user;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void createLoginSession(String id, String email, String password) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_EMAIL, encrypt(email));
        editor.putString(KEY_PASSWORD, encrypt(password));

        // Commit changes
        editor.commit();
    }

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserData() {
        user = new HashMap<>();
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }

    // Gives single value from shared pref
    public String getSharedPrefItem(String dataKey) {
        return pref.getString(dataKey, null);
    }

    // Inserts single value into shared pref
    public void setSharedPrefItem(String dataKey, String dataValue) {
        editor.putString(dataKey, dataValue);
        editor.commit();
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        user.clear();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public static String encrypt(String input) {
        // Base64 encoding
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

}
