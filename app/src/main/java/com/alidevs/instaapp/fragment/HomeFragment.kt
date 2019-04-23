package com.alidevs.instaapp.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.adapter.FullPageAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage

class HomeFragment : Fragment() {

    private lateinit var galleryInactive : ImageView
    private lateinit var uploadImage: ImageView
    private lateinit var galleryActive : ImageView
    private lateinit var pagerActive : ImageView
    private lateinit var pagerInactive : ImageView
    private lateinit var contestInactive : ImageView
    private lateinit var contestActive : ImageView
    private lateinit var recyclerView : RecyclerView
    private lateinit var fullPage : ConstraintLayout


    private lateinit var user_id: String

    private var mainUril: Uri? = null
    private lateinit var storageReference : StorageReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

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
        fullPage = root.findViewById(R.id.full_page)
        uploadImage = root.findViewById(R.id.upload_img)
        //recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = GridLayoutManager(context,3)
        recyclerView.adapter = context?.let { FullPageAdapter(it) }

        firebaseAuth = FirebaseAuth.getInstance()
        user_id = firebaseAuth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        //Click Events
        uploadImage.setOnClickListener {
            CropImage.activity()
                .setMinCropResultSize(512, 512)
                .setAspectRatio(1, 1)
                .start(context!!, this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                user_id = firebaseAuth.currentUser!!.uid
                mainUril = result.uri
                //img_post.setImageURI(mainUril)
                //user_profile.setImageURI(mainUril)
                var img_path = storageReference.child("profile_images").child("$user_id.jpg")

                img_path.putFile(mainUril!!).addOnFailureListener {
                    Toast.makeText(context, "upload failed: " + it.message, Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener { taskSnapshot ->
                    // success
                    img_path.downloadUrl.addOnCompleteListener { taskSnapshot ->
                        storeDataToFireStore(taskSnapshot)
                    }
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
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
        firestore.collection("Posts").document(user_id).collection("images").document()
            .set(items)
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
