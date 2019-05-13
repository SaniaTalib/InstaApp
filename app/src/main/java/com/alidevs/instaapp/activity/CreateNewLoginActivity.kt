package com.alidevs.instaapp.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.utils.AppPreferences
import com.alidevs.instaapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_new_login.*

class CreateNewLoginActivity : AppCompatActivity() {

    private lateinit var utils: AppPreferences
    private lateinit var mAuth: FirebaseAuth
    val networkAvailability: Boolean
        get() = Utils.isNetworkAvailable(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_login)
        if (networkAvailability) {
            mAuth = FirebaseAuth.getInstance()
        }
        var t = Thread(Runnable {
            var getPrefs = PreferenceManager
                .getDefaultSharedPreferences(baseContext);

            //  Create a new boolean and preference and set it to true
            var isFirstStart = getPrefs.getBoolean("firstStart", true);

            if (isFirstStart) {
                val intent = Intent(this@CreateNewLoginActivity, OnBoardActivity::class.java)
                runOnUiThread(Runnable {
                    startActivity(intent)
                })

                val e = getPrefs.edit()

                //  Edit preference to make it false because we don't want this to run again
                e.putBoolean("firstStart", false);

                //  Apply changes
                e.apply();
            }
        })

        t.start()


        utils = AppPreferences(this)

        login_btn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        create_acc_btn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun sendToLogin() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        if (networkAvailability){
            val currentuser = mAuth.currentUser
            if (currentuser != null) {
                sendToLogin()
            }
        }else{
            Toast.makeText(this@CreateNewLoginActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

    }

}
