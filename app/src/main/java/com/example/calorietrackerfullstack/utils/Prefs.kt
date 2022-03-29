package com.example.calorietrackerfullstack.utils
import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    val PREFS_FILENAME = "calorietrackerfullstack.prefs"
    val IS_ADMIN = "isAdmin"
    val USER_ID = "userId"
    val IS_LOGGED_IN = "isLoggedIn"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var isAdminPref: Int
        get() = prefs.getInt(IS_ADMIN, 0)
        set(value) = prefs.edit().putInt(IS_ADMIN, value).apply()

    var userIdPref: Int
        get() = prefs.getInt(USER_ID, 0)
        set(value) = prefs.edit().putInt(USER_ID, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()
}