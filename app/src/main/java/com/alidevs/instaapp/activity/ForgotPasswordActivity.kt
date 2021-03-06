package com.alidevs.instaapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    val networkAvailability : Boolean
        get() = Utils.isNetworkAvailable(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        actionBar!!.title = ""
        actionBar.elevation = 4.0F
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayUseLogoEnabled(true)

        if (networkAvailability){
            mAuth = FirebaseAuth.getInstance()
        }

        send_email_btn.setOnClickListener {
            if (networkAvailability){
                val email = forgot_email.text.toString().trim()
                if (email.isEmpty()) {
                    Toast.makeText(this, "Enter valid Email Address....", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                forgotPassword(email)
            }else{
                Toast.makeText(this@ForgotPasswordActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun forgotPassword(email: String) {
        signup_progress.visibility = View.VISIBLE
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task: Task<Void> ->
            if (task.isSuccessful) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                signup_progress.visibility = View.GONE
                Toast.makeText(applicationContext, "Please Check your Email to reset your password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                signup_progress.visibility = View.GONE
                Toast.makeText(applicationContext, "No user registered with this email", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
