package com.alidevs.instaapp.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alidevs.instaapp.R
import com.alidevs.instaapp.UpdateEmailActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        update_email_settings.setOnClickListener {
            val intent= Intent(this, UpdateEmailActivity::class.java)
            startActivity(intent)

        }

        update_password_settings.setOnClickListener {

            val intent= Intent(this, UpdatePasswordActivity::class.java)
            startActivity(intent)

        }
    }
}
