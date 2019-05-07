package com.alidevs.instaapp.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alidevs.instaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_editwatches.*

class EditwatchesActivity : AppCompatActivity() {

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

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editwatches)

        firebaseAuth = FirebaseAuth.getInstance()
        user_id = firebaseAuth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()

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

        edi_brand.setText(brand)
        edt_model.setText(model)
        edt_ref.setText(ref)
        edt_serial.setText(serial)
        edt_date.setText(pdate)
        edt_comment.setText(comment)

        txt_remove.setOnClickListener {
            firestore.collection("MyWatches/$user_id/submissions").document(id).delete()
            val intent = Intent(this,DashboardActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

    }
}
