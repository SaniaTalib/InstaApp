package com.alidevs.instaapp.activity

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.alidevs.instaapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var user_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        signup_button.setOnClickListener {
            var email = signup_email.text.toString()
            var pass = signup_password.text.toString()
            var c_pass = signup_cnfirm_password.text.toString()

            var name=signup_user_name.text.toString()
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(c_pass) && !TextUtils.isEmpty(name) )
            {
                if (pass == c_pass){
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {task->
                        if (task.isSuccessful){
                            user_id = mAuth.currentUser?.uid!!
                            storeDataToFireStore()
                        }else{
                            Toast.makeText(this,"Error: ${task.exception!!.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this,"Passwords are not matching", Toast.LENGTH_SHORT).show()
                }
            }
            else{

                Toast.makeText(this,"All fields are mandatory",Toast.LENGTH_SHORT).show()
            }
        }









        insta_button.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun storeDataToFireStore() {
        /*val url: Uri

        if (taskSnapShot != null){
            url = taskSnapShot.result!!
        }else{
            url = mainUril!!
        }*/

        //Log.d("FireTAG","$url")
        val items = HashMap<String, Any>()
        //items["user_id"] = user_id
        // items["img_path"] = url.toString()
        items["name"] = signup_user_name.text.toString()
        items["email"] = signup_email.text.toString()

        firestore.collection("users").document(user_id).set(items)
        .addOnCompleteListener {
            if (it.isSuccessful){
                /*val intent = Intent(this@Account, MainActivity::class.java)
                startActivity(intent)
                finish()
                setup_progress.visibility = View.GONE*/

                sendToLogin()
                Toast.makeText(this,"Account Created Successfully..",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Fire Store Error: " + it.exception!!.message, Toast.LENGTH_SHORT).show()
                //   setup_progress.visibility = View.GONE
            }
        }.addOnFailureListener {

        }
    }


    private  fun   sendToLogin()
    {
        val intent=Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        val currentuser = mAuth.currentUser
        if (currentuser != null){
            sendToLogin()
        }
    }
}
