package com.alidevs.instaapp.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.alidevs.instaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_editwatches.*
import kotlinx.android.synthetic.main.activity_editwatches.btn_save
import kotlinx.android.synthetic.main.activity_editwatches.edi_brand
import kotlinx.android.synthetic.main.activity_editwatches.edt_comment
import kotlinx.android.synthetic.main.activity_editwatches.edt_date
import kotlinx.android.synthetic.main.activity_editwatches.edt_model
import kotlinx.android.synthetic.main.activity_editwatches.edt_ref
import kotlinx.android.synthetic.main.activity_editwatches.edt_serial
import java.text.SimpleDateFormat
import java.util.*

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
    var cal = Calendar.getInstance()

    ///
    var txtBrand = ""
    var txtmodel = ""
    var txtref = ""
    var txtserial = ""
    var txtdate = ""
    var txtcomment = ""

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editwatches)

        //Set Action Bar
        val actionBar = supportActionBar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        actionBar?.elevation = 4.0F
        actionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        user_id = firebaseAuth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()

        val bundle = intent.extras
        if (bundle != null) {
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


        btn_save.setOnClickListener {
            setup_progress.visibility = View.VISIBLE
            txtBrand = edi_brand.text.toString()
            txtmodel = edt_model.text.toString()
            txtref = edt_ref.text.toString()
            txtserial = edt_serial.text.toString()
            txtdate = edt_date.text.toString()
            txtcomment = edt_comment.text.toString()


            val docRef = firestore.collection("MyWatches/$user_id/submissions").document(id)

            docRef
                .update(
                    mapOf(
                        "brand_name" to txtBrand,
                        "model" to txtmodel,
                        "reference" to txtref,
                        "serial" to txtserial,
                        "purchase_date" to txtdate,
                        "comments" to txtcomment
                    )
                )
                .addOnSuccessListener {
                    setup_progress.visibility = View.GONE
                    Toast.makeText(this@EditwatchesActivity,"Record Updated Successfully", Toast.LENGTH_SHORT).show()
                    Log.d("EditwatchesActivity", "DocumentSnapshot successfully updated!")
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@EditwatchesActivity,"Error updating document: $e", Toast.LENGTH_SHORT).show()
                    setup_progress.visibility = View.GONE
                    Log.w("EditwatchesActivity", "Error updating document", e)
                }
        }

        txt_remove.setOnClickListener {
            setup_progress.visibility = View.VISIBLE
            firestore.collection("MyWatches/$user_id/submissions").document(id).delete().addOnSuccessListener {
                setup_progress.visibility = View.GONE
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {e->
                setup_progress.visibility = View.GONE
                Toast.makeText(this@EditwatchesActivity,"Error updating document: $e", Toast.LENGTH_SHORT).show()
                Log.w("EditwatchesActivity", "Error updating document", e)
            }

        }
    }


    fun showDatePickerDialog(v: View) {
        DatePickerDialog(
            this@EditwatchesActivity,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateDateInView()
    }

    private fun updateDateInView() {
        val myFormat = "dd MMM yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        edt_date!!.text = sdf.format(cal.time)
    }

}
