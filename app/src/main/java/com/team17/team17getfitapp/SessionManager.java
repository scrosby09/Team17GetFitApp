package com.team17.team17getfitapp;

import com.team17.team17getfitapp.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences Keys
    public static final String PREF_NAME = "GetFitAppPreferences";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // Shared preferences mode
    public static int PRIVATE_MODE = 0;
    private HashMap<String, String> user;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.commit();
    }
    public void createLoginSession(String id, String email, String password) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
//        editor.putString(KEY_EMAIL, encrypt(email));
//        editor.putString(KEY_PASSWORD, encrypt(password));

        // commit changes
        editor.commit();
    }
