package com.alidevs.instaapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var user_id: String = ""
    val networkAvailability: Boolean
        get() = Utils.isNetworkAvailable(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

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

        if (networkAvailability) {
            mAuth = FirebaseAuth.getInstance()
            firestore = FirebaseFirestore.getInstance()
        }


        txt_terms.setOnClickListener {
            val intent = Intent(this@SignupActivity, TermsConditionsActivity::class.java)
            startActivity(intent)
        }


        signup_button.setOnClickListener {
            if (networkAvailability) {
                val email = signup_email.text.toString().trim()
                val pass = signup_password.text.toString().trim()
                val c_pass = signup_cnfirm_password.text.toString().trim()
                val name = signup_user_name.text.toString().trim()
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(c_pass) && !TextUtils.isEmpty(name)){
                    if (pass == c_pass) {
                        if (checkBox.isChecked){
                            signup_progress.visibility = View.VISIBLE
                            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    user_id = mAuth.currentUser?.uid!!
                                    storeDataToFireStore()
                                } else {
                                    signup_progress.visibility = View.GONE
                                    Toast.makeText(this, "Error: ${task.exception!!.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }else{
                            Toast.makeText(this, "You must agreed to our terms and conditions.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        signup_progress.visibility = View.GONE
                        Toast.makeText(this, "Passwords are not matching", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    signup_progress.visibility = View.GONE
                    Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@SignupActivity, "Please check your internet connection.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        insta_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun storeDataToFireStore() {
        val items = HashMap<String, Any>()
        items["name"] = signup_user_name.text.toString().trim()
        items["email"] = signup_email.text.toString().trim()
        items["lastactive"] = FieldValue.serverTimestamp()

        firestore.collection("users").document(user_id).set(items)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    signup_progress.visibility = View.GONE
                    sendToLogin()
                    Toast.makeText(this, "Account Created Successfully..", Toast.LENGTH_SHORT).show()
                } else {
                    signup_progress.visibility = View.GONE
                    Toast.makeText(this, "Fire Store Error: " + it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                signup_progress.visibility = View.GONE
                Toast.makeText(this, "Network: " + it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendToLogin() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        if (networkAvailability) {
            val currentuser = mAuth.currentUser
            if (currentuser != null) {
                sendToLogin()
            }
        } else {
            Toast.makeText(this@SignupActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
