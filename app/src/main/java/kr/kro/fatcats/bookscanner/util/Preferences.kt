package kr.kro.fatcats.bookscanner.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object Preferences {

    private lateinit var prefs: SharedPreferences

    private const val TOKEN_KEY               = "token"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    var token: String
        get() = prefs.getString(TOKEN_KEY, "") ?: ""
        set(value) = prefs.edit {
            putString(TOKEN_KEY, value).apply()
        }
}