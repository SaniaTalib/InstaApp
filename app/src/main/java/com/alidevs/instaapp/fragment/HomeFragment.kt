package com.alidevs.instaapp.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.activity.LoginActivity
import com.alidevs.instaapp.adapter.FullPageAdapter
import com.alidevs.instaapp.adapter.GridViewAdapter
import com.alidevs.instaapp.utils.SnapHelperOneByOne
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class HomeFragment : Fragment() {

    val TAG = HomeFragment::getTag.toString()

    private lateinit var galleryInactive : ImageView
    private lateinit var uploadImage: ImageView
    private lateinit var galleryActive : ImageView
    private lateinit var pagerActive : ImageView
    private lateinit var pagerInactive : ImageView
    private lateinit var contestInactive : ImageView
    private lateinit var contestActive : ImageView
    private lateinit var recyclerView : RecyclerView
    private lateinit var fullPage : RecyclerView
    private lateinit var user_id: String
    private var mainUril: Uri? = null
    private lateinit var storageReference : StorageReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var compressedImageFile: Bitmap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        galleryActive = root.findViewById(R.id.gallery_active)
        galleryInactive = root.findViewById(R.id.gallery_inactive)
        pagerActive = root.findViewById(R.id.pager_active)
        pagerInactive = root.findViewById(R.id.pager_inactive)
        contestInactive = root.findViewById(R.id.award_inactive)
        contestActive = root.findViewById(R.id.award_active)
        recyclerView = root.findViewById(R.id.grid_view) as RecyclerView
        fullPage = root.findViewById(R.id.full_page) as RecyclerView
        uploadImage = root.findViewById(R.id.upload_img)
        //recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = GridLayoutManager(context,3)
        recyclerView.adapter = context?.let { GridViewAdapter(it) }
        fullPage.layoutManager = LinearLayoutManager(context)
        fullPage.adapter = context?.let { FullPageAdapter(it) }

        firebaseAuth = FirebaseAuth.getInstance()
        user_id = firebaseAuth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        /*******************HOME FRAGMENT**********************/
        val linearSnapHelper = PagerSnapHelper()
        linearSnapHelper.attachToRecyclerView(fullPage)

        //Click Events
        uploadImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        context!!.applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(
                        this.activity!!,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
                    )
                } else {
                    selectImage()
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
            } else {
                selectImage()
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
        }

        galleryActive.setOnClickListener {
            galleryActive.visibility = View.GONE
            galleryInactive.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            fullPage.visibility = View.VISIBLE
        }
        galleryInactive.setOnClickListener {
            galleryActive.visibility = View.VISIBLE
            galleryInactive.visibility = View.GONE
            fullPage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        pagerActive.setOnClickListener {
            pagerActive.visibility = View.GONE
            pagerInactive.visibility = View.VISIBLE

        }
        pagerInactive.setOnClickListener {
            pagerActive.visibility = View.VISIBLE
            pagerInactive.visibility = View.GONE
        }
        contestInactive.setOnClickListener {
            contestInactive.visibility = View.GONE
            contestActive.visibility = View.VISIBLE
        }
        contestActive.setOnClickListener {
            contestInactive.visibility = View.VISIBLE
            contestActive.visibility = View.GONE
        }
        return root
    }

    private fun selectImage() {
        CropImage.activity()
            .setMinCropResultSize(512, 512)
            .setAspectRatio(1, 1)
            .start(context!!, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                user_id = firebaseAuth.currentUser!!.uid
                mainUril = result.uri
                //img_post.setImageURI(mainUril)
                //user_profile.setImageURI(mainUril)
                /*var img_path = storageReference.child("profile_images").child("$user_id.jpg")

                img_path.putFile(mainUril!!).addOnFailureListener {
                    Toast.makeText(context, "upload failed: " + it.message, Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener { taskSnapshot ->
                    // success
                    img_path.downloadUrl.addOnCompleteListener { taskSnapshot ->
                        storeDataToFireStore(taskSnapshot)
                    }
                }*/
                compressImage()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d(TAG,"$error")
            }
        }
    }

    private fun compressImage() {
        val randonName = UUID.randomUUID().toString()
        val newimage = File(mainUril!!.path)
        try {
            compressedImageFile = Compressor(this.activity)
                .setMaxHeight(720)
                .setMaxWidth(720)
                .setQuality(50)
                .compressToBitmap(newimage)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val byte = ByteArrayOutputStream()
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, byte)
        val imagedata = byte.toByteArray()

        val ref = storageReference.child("post_images").child("$randonName.jpg")
        val filepath = ref.putBytes(imagedata)
        filepath.addOnFailureListener {
            Toast.makeText(this.activity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            val downloadurl = ref.downloadUrl
            if (it.task.isSuccessful) {
                val newthumbfile = File(mainUril!!.path)
                try {
                    compressedImageFile = Compressor(this.activity)
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
                val ref = storageReference.child("post_images").child("$randonName.jpg")
                val filepath = ref.putBytes(imagedata)

                filepath.addOnFailureListener {
                    Toast.makeText(this.activity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    val downloadurl = ref.downloadUrl
                    if (it.task.isSuccessful) {
                        val newthumbfile = File(mainUril!!.path)
                        try {
                            compressedImageFile = Compressor(this.activity)
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

                        val uploadTask: UploadTask = storageReference.child("post_images/thumbs")
                            .child("$randonName.jpg").putBytes(thumbdata)

                        uploadTask.addOnFailureListener {
                            Toast.makeText(this.activity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener {
                            val downloadthumburl = downloadurl.result
                            val items = java.util.HashMap<String, Any>()
                            items["image_url"] = downloadurl.result.toString()
                            items["image_thumb"] = downloadthumburl.toString()
                            /*items["post_title"] = post_title.text.toString()
                            items["post_description"] = post_description.text.toString()
                            */
                            items["user_id"] = user_id
                            items["timestamp"] = FieldValue.serverTimestamp()

                            firestore.collection("Posts").add(items)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(context, "Post was added", Toast.LENGTH_LONG).show()
                                        /* val intent = Intent(activity, MainActivity::class.java)
                                         activity!!.startActivity(intent)
                                         activity!!.finish()*/
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(context, "FireStore Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            sendToLogin()
        }
    }

    private fun sendToLogin() {
        val intent = Intent(activity!!, LoginActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }

    private fun storeDataToFireStore(taskSnapShot: Task<Uri>?) {
        val url: Uri

        if (taskSnapShot != null){
            url = taskSnapShot.result!!
        }else{
            url = mainUril!!
        }

        Log.d("FireTAG","$url")
        val items = HashMap<String, Any>()
        items["user_id"] = user_id
        items["img_path"] = url.toString()
        firestore.collection("Posts").document().set(items)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    /*val intent = Intent(this@Account, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    setup_progress.visibility = View.GONE*/
                    Toast.makeText(context,"Account Updated Successfully..",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Fire Store Error: " + it.exception!!.message, Toast.LENGTH_SHORT).show()
                    //setup_progress.visibility = View.GONE
                }
            }
    }
}
