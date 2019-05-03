package com.alidevs.instaapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.alidevs.instaapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

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
        actionBar.setLogo(R.drawable.ic_logo)
        actionBar.setDisplayUseLogoEnabled(true)

        mAuth = FirebaseAuth.getInstance()

        send_email_btn.setOnClickListener {

            val email = forgot_email.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter valid Email Address....", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            forgotPassword(email)
        }

    }

    private fun forgotPassword(email: String) {

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task: Task<Void> ->
            if (task.isSuccessful) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                Toast.makeText(applicationContext, "Please Check your Email to reset your password", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}
