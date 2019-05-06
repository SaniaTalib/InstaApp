package com.alidevs.instaapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.alidevs.instaapp.activity.LoginActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_update_email.*

class UpdateEmailActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var user_id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_email)

        setSupportActionBar(update_email_toolbar)

        mAuth = FirebaseAuth.getInstance()
        user_id = mAuth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()

        val actionBar = supportActionBar
        update_email_toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        update_email_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        actionBar!!.title = ""
        actionBar.elevation = 4.0F
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayUseLogoEnabled(true)


        mAuth = FirebaseAuth.getInstance()
        update_email_btn.setOnClickListener {
            updateEmailAddress()
        }
    }

    private fun updateEmailAddress() {

        if (update_email_address.text.isNotEmpty() && current_password.text.isNotEmpty()) {
            val user: FirebaseUser? = mAuth.currentUser
            if (user != null && user.email != null) {
                val newEmail = update_email_address.text
                val confirmNewEmail = update_confirm_email_address.text
                if(newEmail.trim() == confirmNewEmail.trim()){
                    signup_progress.visibility = View.VISIBLE

                    val credential = EmailAuthProvider.getCredential(user.email!!, current_password.text.toString())

                    user.reauthenticate(credential)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            user.updateEmail(update_email_address.text.toString())
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        signup_progress.visibility = View.GONE
                                        Toast.makeText(this, "Email change success", Toast.LENGTH_SHORT).show()
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
                }else{
                    signup_progress.visibility = View.GONE
                    Toast.makeText(this, "Email address doesn't match", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser?.uid
        if (currentUser == null) {
            sendToLogin1()
        }else{
            firestore!!.collection("users").document(currentUser).update("lastactive", FieldValue.serverTimestamp())
        }
    }

    private fun sendToLogin1() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
