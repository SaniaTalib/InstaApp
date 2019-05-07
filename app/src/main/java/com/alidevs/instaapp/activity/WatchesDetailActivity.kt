package com.alidevs.instaapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alidevs.instaapp.R
import com.alidevs.instaapp.model.WatchesModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_watches_detail.*

class WatchesDetailActivity: AppCompatActivity() {


    private lateinit var storageReference: StorageReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private var user_id: String = ""
    private var brand: String = ""
    private var ref: String = ""
    private var serial: String = ""
    private var pdate: String = ""
    private var primaryImg: String = ""
    private var firstImg: String = ""
    private var secondImg: String = ""
    private var thirdImg: String = ""
    private var comment: String = ""
    private var model: String = ""
    private var id: String = ""
    private lateinit var posts_list: MutableList<WatchesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watches_detail)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        actionBar!!.title = ""
        actionBar.elevation = 4.0F
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayUseLogoEnabled(true)
        //Init components
        firebaseAuth = FirebaseAuth.getInstance()
        user_id = firebaseAuth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        posts_list = ArrayList()
        val bundle=intent.extras

        if(bundle!=null)
        {
            id = bundle.getString("post_id")
            ref = bundle.getString("reference")
            serial = bundle.getString("serial")
            pdate = bundle.getString("pdate")
            model = bundle.getString("model")
            primaryImg = bundle.getString("primary_img")
            firstImg = bundle.getString("first_img")
            secondImg = bundle.getString("second_img")
            thirdImg = bundle.getString("third_img")
            comment = bundle.getString("comments")
            brand = bundle.getString("brand_name")
        }

        //set values to views
        txt_ref.text = ref
        txt_serial.text = serial
        txt_pdate.text = pdate
        txt_comment.text = comment
        title_brand.text = brand

        Glide.with(this)
            .load(primaryImg)
            .into(img_primary)

        Glide.with(this)
            .load(firstImg)
            .into(img_one)

        Glide.with(this)
            .load(secondImg)
            .into(img_two)

        Glide.with(this)
            .load(thirdImg)
            .into(img_three)

        txt_edit.setOnClickListener {
            val intent = Intent(this@WatchesDetailActivity, EditwatchesActivity::class.java)
            intent.putExtra("reference", ref)
            intent.putExtra("brand_name", brand)
            intent.putExtra("post_id", id)
            intent.putExtra("model", model)
            intent.putExtra("serial", serial)
            intent.putExtra("pdate", pdate)
            intent.putExtra("comments", comment)
            intent.putExtra("primary_img", primaryImg)
            intent.putExtra("first_img", firstImg)
            intent.putExtra("second_img", secondImg)
            intent.putExtra("third_img", thirdImg)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser?.uid
        if (currentUser == null) {
            sendToLogin()
        }else{
            firestore!!.collection("users").document(currentUser).update("lastactive", FieldValue.serverTimestamp())
        }
    }

    private fun sendToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}