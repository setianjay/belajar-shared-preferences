package com.setianjay.mysharedpreferences

import android.content.Context

class UserPreferences(context: Context) {
    private val pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    companion object{
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val AGE = "age"
        private const val PHONE_NUMBER = "phone_number"
        private const val LOVE_MU = "islove"
    }

    fun setUser(value: UserModel){
        editor.putString(NAME, value.name)
        editor.putString(EMAIL, value.email)
        editor.putInt(AGE, value.age)
        editor.putString(PHONE_NUMBER, value.phoneNumber)
        editor.putBoolean(LOVE_MU, value.isLove)
        editor.apply()
    }

    fun getUser(): UserModel{
        val model = UserModel()
        model.name = pref.getString(NAME, "")
        model.email = pref.getString(EMAIL, "")
        model.age = pref.getInt(AGE, 0)
        model.phoneNumber = pref.getString(PHONE_NUMBER, "")
        model.isLove = pref.getBoolean(LOVE_MU, false)

        return model
    }
}