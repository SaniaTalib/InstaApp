package com.alidevs.instaapp.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alidevs.instaapp.R
import com.alidevs.instaapp.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_create_new_login.*

class CreateNewLoginActivity : AppCompatActivity() {

    private lateinit var utils: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_login)

        utils = AppPreferences(this)

        login_btn.setOnClickListener {
            val intent =Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        create_acc_btn.setOnClickListener {
            val intent= Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

}
