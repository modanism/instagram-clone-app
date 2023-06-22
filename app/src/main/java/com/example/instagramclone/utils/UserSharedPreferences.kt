package com.example.instagramclone.utils

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class UserSharedPreferences(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val TOKEN = "token"
        private const val STATE = "state"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(TOKEN, value.token)
        editor.putBoolean(STATE, value.isLogin)
        editor.apply()
    }

    fun getUser(): UserModel {
        val model = UserModel()
        model.name = preferences.getString(NAME, "")
        model.token = preferences.getString(TOKEN, "")
        model.isLogin = preferences.getBoolean(STATE, false)

        return model
    }

    fun clearUser() {
        preferences.edit().clear().apply()
    }
}

@Parcelize
data class UserModel(
    var name: String? = null,
    var token: String? = null,
    var isLogin: Boolean = false
) : Parcelable