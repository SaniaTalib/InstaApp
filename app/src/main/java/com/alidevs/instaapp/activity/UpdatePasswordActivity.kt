package com.alidevs.instaapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.alidevs.instaapp.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_update_password.*

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        setSupportActionBar(update_password_toolbar)
        val actionBar = supportActionBar
        update_password_toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        actionBar!!.title = ""
        actionBar.elevation = 4.0F
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setLogo(R.drawable.ic_logo)
        actionBar.setDisplayUseLogoEnabled(true)
        mAuth = FirebaseAuth.getInstance()
        update_password_btn.setOnClickListener {
            changePassword()
        }
    }


    private fun changePassword() {
        if (update_existing_password.text.isNotEmpty() && update_new_password.text.isNotEmpty()
            && update_confirm_password.text.isNotEmpty()
        ) {
            if (update_new_password.text.toString() == update_confirm_password.text.toString()) {
                val user: FirebaseUser? = mAuth.currentUser
                if (user != null && user.email != null) {

                    val credential =
                        EmailAuthProvider.getCredential(user.email!!, update_existing_password.text.toString())

                    user.reauthenticate(credential)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Updated.....", Toast.LENGTH_SHORT).show()

                            user?.updatePassword(update_new_password.text.toString())
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(this, "password change success", Toast.LENGTH_SHORT).show()

                                        mAuth.signOut()

                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Not Updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(this, "Password mismatch.....", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "Please Fill all .....", Toast.LENGTH_SHORT).show()
        }
    }

}
