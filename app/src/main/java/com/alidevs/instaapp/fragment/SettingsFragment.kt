package com.alidevs.instaapp.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.UpdateEmailActivity
import com.alidevs.instaapp.activity.LoginActivity
import com.alidevs.instaapp.activity.UpdatePasswordActivity
import com.alidevs.instaapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment : Fragment() {

    private lateinit var updateEmail: TextView
    private lateinit var updatePassword: TextView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user_id: String
    val networkAvailability : Boolean
        get() = Utils.isNetworkAvailable(context!!)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.activity_settings, container, false)

        updateEmail = root.findViewById(R.id.update_email_settings)
        updatePassword = root.findViewById(R.id.update_password_settings)

        if (networkAvailability){
            firebaseAuth = FirebaseAuth.getInstance()
            user_id = firebaseAuth.currentUser!!.uid
            firestore = FirebaseFirestore.getInstance()
        }

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

    override fun onStart() {
        super.onStart()
        if (networkAvailability){
            val currentUser = firebaseAuth.currentUser?.uid
            if (currentUser == null) {
                sendToLogin()
            }
        }else{
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

    }

    private fun sendToLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}
