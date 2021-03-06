package com.alidevs.instaapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.ViewModel.AuthenticationListner
import com.alidevs.instaapp.ViewModel.GetData
import com.alidevs.instaapp.ViewModel.InstagramDialog
import com.alidevs.instaapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), AuthenticationListner {

    private lateinit var mAuth: FirebaseAuth
    private var instagramdialog: InstagramDialog? = null
    val networkAvailability: Boolean
        get() = Utils.isNetworkAvailable(applicationContext)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@LoginActivity, CreateNewLoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        actionBar!!.title = ""
        actionBar.elevation = 4.0F
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayUseLogoEnabled(true)

        if (networkAvailability) {
            mAuth = FirebaseAuth.getInstance()
        }

        btn_add_watches.setOnClickListener {
            if (networkAvailability) {
                val stringEmail = user_name.text.toString().trim()
                val stringPass = login_password.text.toString().trim()

                if (!TextUtils.isEmpty(stringEmail) && !TextUtils.isEmpty(stringPass)) {
                    signup_progress.visibility = View.VISIBLE
                    mAuth.signInWithEmailAndPassword(stringEmail, stringPass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            signup_progress.visibility = View.GONE
                            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                            startActivity(intent)
                        } else {
                            signup_progress.visibility = View.GONE
                            Toast.makeText(this, "Error: ${task.exception!!.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    signup_progress.visibility = View.GONE
                    Toast.makeText(this, "Fill all Fields", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this@LoginActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }


        forgot_pass.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        insta_login_button.setOnClickListener {
            instagramdialog = InstagramDialog(this, this)
            instagramdialog!!.setCancelable(true)
            instagramdialog!!.show()

            val window = instagramdialog!!.window
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun OnCodeRecieved(auth_token: String) {
        if (auth_token != null) {
            Log.d("accetoken", auth_token)
            val getdata = GetData(this)
            getdata.getUserinfo(auth_token)
        }
    }

    override fun DataRecieved(data: MutableMap<String, String>) {
        val userName = data["username"]
        Log.d("Data", userName)
        Toast.makeText(this, "User Logedin: $userName", Toast.LENGTH_SHORT).show()
    }

    private fun sendToLogin() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()

        if (networkAvailability) {
            val currentuser = mAuth.currentUser
            if (currentuser != null) {
                sendToLogin()
            }
        } else {
            Toast.makeText(this@LoginActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@LoginActivity, CreateNewLoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
