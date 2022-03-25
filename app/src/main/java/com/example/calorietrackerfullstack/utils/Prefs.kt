package com.example.calorietrackerfullstack.utils
import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    val PREFS_FILENAME = "calorietrackerfullstack.prefs"
    val IS_ADMIN = "isAdmin"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var isAdminPref: Int
        get() = prefs.getInt(IS_ADMIN, 0)
        set(value) = prefs.edit().putInt(IS_ADMIN, value).apply()
}