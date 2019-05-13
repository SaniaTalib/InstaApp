package com.alidevs.instaapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.utils.Utils
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_update_password.*

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    val networkAvailability : Boolean
        get() = Utils.isNetworkAvailable(applicationContext)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        setSupportActionBar(update_password_toolbar)
        val actionBar = supportActionBar

        if (networkAvailability){
            mAuth = FirebaseAuth.getInstance()
            firestore = FirebaseFirestore.getInstance()
        }

        update_password_toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        update_password_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        actionBar!!.title = ""
        actionBar.elevation = 4.0F
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayUseLogoEnabled(true)
        update_password_btn.setOnClickListener {
            if (networkAvailability){
                changePassword()
            }else{
                Toast.makeText(this@UpdatePasswordActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private fun changePassword() {
        if (update_existing_password.text.isNotEmpty() && update_new_password.text.isNotEmpty()
            && update_confirm_password.text.isNotEmpty()
        ) {
            if (update_new_password.text.toString() == update_confirm_password.text.toString()) {
                signup_progress.visibility = View.VISIBLE
                val user: FirebaseUser? = mAuth.currentUser
                if (user != null && user.email != null) {

                    val credential =
                        EmailAuthProvider.getCredential(user.email!!, update_existing_password.text.toString())

                    user.reauthenticate(credential)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            user?.updatePassword(update_new_password.text.toString())
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        signup_progress.visibility = View.GONE
                                        Toast.makeText(this, "password change success", Toast.LENGTH_SHORT).show()

                                        mAuth.signOut()

                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    }
                                }
                        } else {
                            signup_progress.visibility = View.GONE
                            Toast.makeText(this, "Not Updated->${it.result}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    signup_progress.visibility = View.GONE
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            } else {
                signup_progress.visibility = View.GONE
                Toast.makeText(this, "Password mismatch.....", Toast.LENGTH_SHORT).show()
            }

        } else {
            signup_progress.visibility = View.GONE
            Toast.makeText(this, "Please Fill all .....", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        if (networkAvailability){
            val currentUser = mAuth.currentUser?.uid
            if (currentUser == null) {
                sendToLogin1()
            }
        }else{
            Toast.makeText(this@UpdatePasswordActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private fun sendToLogin1() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
