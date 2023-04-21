package com.febiarifin.storyappsubmissiondicoding.utils

import android.content.Context
import com.febiarifin.storyappsubmissiondicoding.data.model.Login

class UserPreference(context: Context) {
    companion object{
        private const val PREFERENCE_NAME = "user_preference"
        private const val NAME = "name"
        private const val USER_ID = "user_id"
        private const val TOKEN = "token"
    }

    private val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setUser(value: Login){
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(USER_ID, value.userId)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUserName(): String?{
        return preferences.getString(NAME, null)
    }

    fun getUserToken(): String?{
        return preferences.getString(TOKEN, null)
    }

    fun clearPreference(){
        val editor = preferences.edit()
        editor.remove(NAME)
        editor.remove(USER_ID)
        editor.remove(TOKEN)
        editor.apply()
    }
}