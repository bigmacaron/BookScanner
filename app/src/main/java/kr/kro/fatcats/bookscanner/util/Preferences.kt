package kr.kro.fatcats.bookscanner.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object Preferences {

    private lateinit var prefs: SharedPreferences

    private const val CERT_KEY               = "certKey" //중앙도서관 api 인증키

    fun init(context: Context) {
        prefs = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    var certKey: String
        get() = prefs.getString(CERT_KEY, "") ?: ""
        set(value) = prefs.edit {
            putString(CERT_KEY, value).apply()
        }
}