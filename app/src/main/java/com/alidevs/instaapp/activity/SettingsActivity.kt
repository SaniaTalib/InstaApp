package com.alidevs.instaapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alidevs.instaapp.R
import com.alidevs.instaapp.UpdateEmailActivity

class SettingsActivity : Fragment() {

    private lateinit var updateEmail: TextView
    private lateinit var updatePassword: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.activity_settings, container, false)

        updateEmail = root.findViewById(R.id.update_email_settings)
        updatePassword = root.findViewById(R.id.update_password_settings)

        updateEmail.setOnClickListener {
            val intent= Intent(context, UpdateEmailActivity::class.java)
            startActivity(intent)
        }

        updatePassword.setOnClickListener {
            val intent= Intent(context, UpdatePasswordActivity::class.java)
            startActivity(intent)
        }
        return root
    }
}
