package com.alidevs.instaapp.utils

import android.content.Context
import android.content.SharedPreferences
import java.text.DateFormat.getDateTimeInstance
import java.text.SimpleDateFormat
import java.util.*

/*public class AppPreferences {

    private lateinit var preferences:SharedPreferences



    fun clear()
    {
        var editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}*/


class AppPreferences(context: Context) {
    var preferences: SharedPreferences
    val APP_PREFERENCES_FILE_NAME = "userdata"


    private var editor: SharedPreferences.Editor

    init {
        AppPreferences.context = context
        preferences = context.getSharedPreferences("util", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }



    fun putString(key: String, value: String) {
        var editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }
    fun getDateTime(): String {
        val dateFormat = getDateTimeInstance()
        val date = Date()
        return dateFormat.format(date)
    }
    /* fun savedata(key: String, `val`: String) {
        editor.putString(key, `val`).commit()
    }*/

    /* fun getdata(key: String): String {
         val value = preferences.getString(key, "")
         if (value!!.isEmpty()) {
             Log.i(TAG, "$key not found.")
             return ""
         }
         return value
     }*/


    companion object {
        internal lateinit var context: Context
        const val TAG = "SP"
        val USER_ID = "userID"
        val TOKEN = "token"
        val PROFILE_PIC = "profile_pic"
        val USER_NAME = "username"
    }
}
