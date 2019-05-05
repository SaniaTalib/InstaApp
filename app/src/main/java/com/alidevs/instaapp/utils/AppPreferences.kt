package com.alidevs.instaapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import java.text.DateFormat.getDateTimeInstance
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
    fun getCurrentDate(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm:ss")
            val answer = current.format(formatter)
            answer
        } else {
            val date = Date()
            val formatter = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss")
            val answer: String = formatter.format(date)
            answer
        }
    }

    fun getDate(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
            val answer = current.format(formatter)
            answer
        } else {
            val date = Date()
            val formatter = SimpleDateFormat("dd-MMM-yyyy")
            val answer: String = formatter.format(date)
            answer
        }
    }

    fun getDate1(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val answer = current.format(formatter)
            answer
        } else {
            val date = Date()
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val answer: String = formatter.format(date)
            answer
        }
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
