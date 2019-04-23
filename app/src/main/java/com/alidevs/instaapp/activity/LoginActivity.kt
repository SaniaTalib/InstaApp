package com.alidevs.instaapp.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.alidevs.instaapp.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        login_button.setOnClickListener {
            val stringEmail =user_name.text.toString()
            val stringPass = login_password.text.toString()

            if (!TextUtils.isEmpty(stringEmail) && !TextUtils.isEmpty(stringPass)){
                mAuth.signInWithEmailAndPassword(stringEmail,stringPass).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        val intent = Intent(this@LoginActivity,DashboardActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,"Error: ${task.exception!!.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                Toast.makeText(this,"Fill all Fields", Toast.LENGTH_LONG).show()
            }
        }



        insta_login_button.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}


/*
package com.alidevs.instaapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.wanghong.kostagram.api.EndPoints
import com.wanghong.kostagram.model.User
import kotlinx.android.synthetic.main.activity_login.*
import android.view.View
import android.widget.Button

import android.os.AsyncTask.execute

import android.widget.Toast
import org.json.JSONException
import android.os.AsyncTask
import com.alidevs.instaapp.R
import com.alidevs.instaapp.utils.AppPreferences
import com.alidevs.instaapp.utils.AuthenticationListener
import org.json.JSONObject
import java.io.IOException


class LoginActivity : AppCompatActivity() {

    private var token: String? = null
    private var appPreferences: AppPreferences? = null
    //private var authenticationDialog: AuthenticationDialog? = null
    private val button: Button? = null
    private val info: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        appPreferences = AppPreferences(this)

        //check already have access token
        token = appPreferences!!.getString(AppPreferences.TOKEN)
       */
/* if (token != null) {
            getUserInfoByAccessToken(token!!)
        }*//*


        login_button.setOnClickListener {
            val intent= Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        insta_login_button.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
            */
/*if (token != null) {
                logout()
            } else {
                authenticationDialog = AuthenticationDialog(this, this)
                authenticationDialog?.setCancelable(true)
                authenticationDialog?.show()
            }*//*

        }
    }


  */
/*  override fun onTokenReceived(auth_token: String) {
        if (auth_token == null)
            return
        appPreferences!!.putString(AppPreferences.TOKEN, auth_token)
        token = auth_token
        getUserInfoByAccessToken(token!!)
    }

    private fun getUserInfoByAccessToken(token: String) {
        RequestInstagramAPI().execute()
    }

    private inner class RequestInstagramAPI : AsyncTask<Void, String, String>() {

        override fun doInBackground(vararg params: Void): String? {
            val httpClient = DefaultHttpClient()
            val httpGet = HttpGet(resources.getString(com.alidevs.instaapp.R.string.get_user_info_url) + token)
            try {
                val response = httpClient.execute(httpGet)
                val httpEntity = response.getEntity()
                return EntityUtils.toString(httpEntity)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(response: String?) {
            super.onPostExecute(response)
            if (response != null) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.e("response", jsonObject.toString())
                    val jsonData = jsonObject.getJSONObject("data")
                    if (jsonData.has("id")) {
                        //сохранение данных пользователя
                        appPreferences!!.putString(AppPreferences.USER_ID, jsonData.getString("id"))
                        appPreferences!!.putString(AppPreferences.USER_NAME, jsonData.getString("username"))
                        appPreferences!!.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"))
                        login()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            } else {
                val toast = Toast.makeText(applicationContext, "Ошибка входа!", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }*//*


  */
/*  fun logout() {
        //button.setText("INSTAGRAM LOGIN")
        token = null
        //info.setVisibility(View.GONE)
        appPreferences.clear()
    }*//*

}
*/
