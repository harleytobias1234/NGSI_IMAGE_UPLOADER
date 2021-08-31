package com.example.ngsi_image_uploader.Component

import android.content.Context
import android.content.Intent

import com.example.ngsi_image_uploader.Activities.Login.LoginActivity

import android.content.SharedPreferences


class SessionManager(context: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var _context: Context
    var Private_mode = 0
    fun createLoginSession(email: String?, password: String?) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_PASSWORD, password)
        editor.commit()
    }

    val isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGIN, false)

    fun checkLogin() {
        if (!isLoggedIn) {
            val i = Intent(_context, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK )
            _context.startActivity(i)
        }
    }

    val userDetails: HashMap<String, String?>
        get() {
            val user = HashMap<String, String?>()
            user[KEY_EMAIL] =
                pref.getString(KEY_EMAIL, null)
            user[KEY_PASSWORD] =
                pref.getString(KEY_PASSWORD, null)
            return user
        }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        val i = Intent(_context, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        _context.startActivity(i)
    }

    companion object {
        private const val PREF_NAME = "AndroidHivePref"
        private const val IS_LOGIN = "IsLoggedIn"
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"
    }

    init {
        _context = context
        pref = _context.getSharedPreferences(PREF_NAME, Private_mode)
        editor = pref.edit()
    }
}