package com.alidevs.instaapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alidevs.instaapp.activity.LoginActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_update_email.*
import kotlinx.android.synthetic.main.activity_update_password.*

class UpdateEmailActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_email)

        setSupportActionBar(update_email_toolbar)


        val actionBar = supportActionBar
        update_email_toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        actionBar!!.title = ""
        actionBar.elevation = 4.0F
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setLogo(R.drawable.ic_logo)
        actionBar.setDisplayUseLogoEnabled(true)


        mAuth = FirebaseAuth.getInstance()
        update_email_btn.setOnClickListener {

            updateEmailAddress()
        }




    }

    private fun  updateEmailAddress()
    {

        if (update_email_address.text.isNotEmpty() && current_password.text.isNotEmpty())
        {
        val user: FirebaseUser? = mAuth.currentUser
        if (user != null && user.email != null) {

            val credential =
                EmailAuthProvider.getCredential(user.email!!, current_password.text.toString() )

            user.reauthenticate(credential)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Updated.....", Toast.LENGTH_SHORT).show()

                    user?.updateEmail(update_email_address.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Email change success", Toast.LENGTH_SHORT).show()

                                mAuth.signOut()



                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                }
                else {
                    Toast.makeText(this, "Not Updated", Toast.LENGTH_SHORT).show()
                }
            }
        }  }
    }
}
