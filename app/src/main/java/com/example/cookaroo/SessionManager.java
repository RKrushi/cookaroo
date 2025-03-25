package com.example.cookaroo;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "CookarooPrefs";
    private static final String KEY_USER_ID = "USER_ID";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save user ID to session
    public void saveUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    // Retrieve user ID
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return getUserId() != null;
    }

    // Clear session (logout)
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
