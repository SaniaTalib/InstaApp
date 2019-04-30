package com.alidevs.instaapp.fragment

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.utils.AppPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.fragment_add_watches.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class AddWatchesFragment : AppCompatActivity() {

    private val TAG = "AddWatchesFragment"

    private lateinit var storageReference: StorageReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var compressedImageFile: Bitmap
    private lateinit var user_id: String
    private lateinit var utils: AppPreferences


    private lateinit var primaryImageURI: Uri
    private lateinit var firstImageURI: Uri
    private lateinit var secondImageURI: Uri
    private lateinit var thirdImageURI: Uri
    private var primary = false
    private var first = false
    private var second = false
    private var third = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_watches)

        //Set Action Bar
        val actionBar = supportActionBar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        actionBar?.elevation = 4.0F
        actionBar?.setDisplayShowHomeEnabled(true)

        //Init components
        firebaseAuth = FirebaseAuth.getInstance()
        user_id = firebaseAuth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        utils = AppPreferences(this)


        img_primary.setOnClickListener {
            primary = true
            selectImage()
        }

        img_one.setOnClickListener {
            first = true
            selectImage()
        }
        img_two.setOnClickListener {
            second = true
            selectImage()
        }
        img_three.setOnClickListener {
            third = true
            selectImage()
        }

        btn_save.setOnClickListener {
            /*sendDataToStore(primaryImageURI)
            sendDataToStore(firstImageURI)
            sendDataToStore(secondImageURI)
            sendDataToStore(thirdImageURI)*/
        }
    }

    private fun selectImage() {
        CropImage.activity()
            .setMinCropResultSize(512, 512)
            .setAspectRatio(1, 1)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                //val resultUri = result.uri
                if (primary) {
                    primaryImageURI = result.uri
                    img_primary.visibility = View.GONE
                    val f = File(getRealPathFromURI(primaryImageURI))
                    val d = Drawable.createFromPath(f.absolutePath)
                    constraintLayout3.background = d
                    primary = false
                }
                if (first) {
                    firstImageURI = result.uri
                    img_one.visibility = View.GONE
                    val f = File(getRealPathFromURI(primaryImageURI))
                    val d = Drawable.createFromPath(f.absolutePath)
                    first_img.background = d
                    first = false
                }
                if (second) {
                    secondImageURI = result.uri
                    img_two.visibility = View.GONE
                    val f = File(getRealPathFromURI(primaryImageURI))
                    val d = Drawable.createFromPath(f.absolutePath)
                    second_img.background = d
                    second = false
                }
                if (third) {
                    thirdImageURI = result.uri
                    img_three.visibility = View.GONE
                    val f = File(getRealPathFromURI(primaryImageURI))
                    val d = Drawable.createFromPath(f.absolutePath)
                    third_img.background = d
                    third = false
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d(TAG, error.toString())
            }
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val cursor: Cursor? =  contentResolver.query(contentURI, null, null, null, null)
        return if (cursor == null) { // Source is Dropbox or other similar local file path
            contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }



    private fun sendDataToStore() {
        val randonName = UUID.randomUUID().toString()
        val primaryImgage = File(primaryImageURI.path)
        /*val newimage = File(primaryImageURI.path)
        val newimage = File(primaryImageURI.path)
        val newimage = File(primaryImageURI.path)*/

        try {
            compressedImageFile = Compressor(this)
                .setMaxHeight(720)
                .setMaxWidth(720)
                .setQuality(50)
                .compressToBitmap(primaryImgage)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val byte = ByteArrayOutputStream()
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, byte)
        val imagedata = byte.toByteArray()
        val ref = storageReference.child("watches_post_images").child("$randonName.jpg")
        val filepath = ref.putBytes(imagedata)
        filepath.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            val downloadurl = ref.downloadUrl
            if (it.task.isSuccessful) {
                val newthumbfile = File(primaryImgage.path)
                try {
                    compressedImageFile = Compressor(this)
                        .setMaxHeight(100)
                        .setMaxWidth(100)
                        .setQuality(1)
                        .compressToBitmap(newthumbfile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val byte = ByteArrayOutputStream()
                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, byte)
                val thumbdata = byte.toByteArray()
                val ref = storageReference.child("watches_post_images").child("$randonName.jpg")
                val filepath = ref.putBytes(imagedata)
                filepath.addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    val downloadurl = ref.downloadUrl
                    if (it.task.isSuccessful) {
                        val newthumbfile = File(primaryImgage.path)
                        try {
                            compressedImageFile = Compressor(this)
                                .setMaxHeight(100)
                                .setMaxWidth(100)
                                .setQuality(1)
                                .compressToBitmap(newthumbfile)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        val byte = ByteArrayOutputStream()
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, byte)
                        val thumbdata = byte.toByteArray()
                        val uploadTask: UploadTask = storageReference.child("watches_post_images/thumbs")
                            .child("$randonName.jpg").putBytes(thumbdata)





                        uploadTask.addOnFailureListener {
                            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener {
                            val downloadthumburl = downloadurl.result
                            val items = HashMap<String, Any>()
                            items["image_url_primary"] = downloadurl.result.toString()
                            items["image_thumb_primary"] = downloadthumburl.toString()
                            items["image_url_first"] = downloadurl.result.toString()
                            items["image_thumb_first"] = downloadthumburl.toString()
                            items["image_url_second"] = downloadurl.result.toString()
                            items["image_thumb_second"] = downloadthumburl.toString()
                            items["image_url_third"] = downloadurl.result.toString()
                            items["image_thumb_third"] = downloadthumburl.toString()
                            items["reference"] = downloadthumburl.toString()
                            items["timestamp"] = FieldValue.serverTimestamp()

                            firestore.collection("Posts").add(items)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Post was added", Toast.LENGTH_LONG).show()
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(this, "FireStore Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
            }
        }
    }
}
