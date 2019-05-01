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
import com.alidevs.instaapp.adapter.LeaderBoardAdapter
import com.alidevs.instaapp.model.PostsModel
import com.alidevs.instaapp.utils.AppPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    val TAG = HomeFragment::getTag.toString()
    private lateinit var utils: AppPreferences

    private lateinit var galleryInactive: ImageView
    private lateinit var uploadImage: ImageView
    private lateinit var galleryActive: ImageView
    private lateinit var pagerActive: ImageView
    private lateinit var pagerInactive: ImageView
    private lateinit var contestInactive: ImageView
    private lateinit var contestActive: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fullPage: RecyclerView
    private lateinit var leaderBoard: RecyclerView
    private lateinit var user_id: String
    private var mainUril: Uri? = null
    private lateinit var storageReference: StorageReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var compressedImageFile: Bitmap
    private lateinit var posts_list: MutableList<PostsModel>
    private lateinit var leaderboard_list: MutableList<PostsModel>
    private var postsAdaptert: FullPageAdapter? = null
    private lateinit var arrayList: ArrayList<PostsModel>


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
        leaderBoard = root.findViewById(R.id.recycler_leaderboard) as RecyclerView
        uploadImage = root.findViewById(R.id.upload_img)
        posts_list = ArrayList()
        leaderboard_list = ArrayList()
        firebaseAuth = FirebaseAuth.getInstance()
        user_id = firebaseAuth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        utils = AppPreferences(context!!)
        arrayList = ArrayList()

        loadPosts()
        loadLeaderboardPosts()

        /*******************HOME FRAGMENT**********************/
        val linearSnapHelper = PagerSnapHelper()
        linearSnapHelper.attachToRecyclerView(fullPage)
        val linearSnapHelper1 = PagerSnapHelper()
        linearSnapHelper1.attachToRecyclerView(leaderBoard)
        fullPage.layoutManager = LinearLayoutManager(context)
        fullPage.adapter = context?.let { FullPageAdapter(context!!, posts_list) }
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = context?.let { GridViewAdapter(posts_list) }
        leaderBoard.layoutManager = LinearLayoutManager(context)
        leaderBoard.adapter = context?.let { LeaderBoardAdapter(context!!, leaderboard_list) }

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

        galleryInactive.setOnClickListener {
            galleryActive.visibility = View.VISIBLE
            pagerInactive.visibility = View.VISIBLE
            contestInactive.visibility = View.VISIBLE

            galleryInactive.visibility = View.GONE
            pagerActive.visibility = View.GONE
            contestActive.visibility = View.GONE

            fullPage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            leaderBoard.visibility = View.GONE
        }

        pagerInactive.setOnClickListener {

            pagerActive.visibility = View.VISIBLE
            galleryInactive.visibility = View.VISIBLE
            contestInactive.visibility = View.VISIBLE

            pagerInactive.visibility = View.GONE
            galleryActive.visibility = View.GONE
            contestActive.visibility = View.GONE


            fullPage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

            leaderBoard.visibility = View.GONE
        }

        contestInactive.setOnClickListener {
            contestInactive.visibility = View.GONE

            pagerInactive.visibility = View.VISIBLE
            galleryInactive.visibility = View.VISIBLE
            contestActive.visibility = View.VISIBLE
            leaderBoard.visibility = View.VISIBLE

            fullPage.visibility = View.GONE
            recyclerView.visibility = View.GONE
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
                compressImage()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d(TAG, "$error")
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
                            val items = HashMap<String, Any>()
                            val dateString = AppPreferences(context!!).getCurrentDate()
                            //DocumentReference userRef = db.collection("company").document("users");
                            //val userRef = firestore.collection("users/$user_id")
                            items["image_url"] = downloadurl.result.toString()
                            items["image_thumb"] = downloadthumburl.toString()
                            items["reference"] = downloadthumburl.toString()
                            items["date_time"] = dateString.toString()
                            items["timestamp"] = FieldValue.serverTimestamp()
                            items["likes_count"] = 0

                            firestore.collection("Posts").add(items)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(context, "Post was added", Toast.LENGTH_LONG).show()
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

    private fun loadPosts() {

        try {
            val dateString = AppPreferences(context!!).getCurrentDate()
            val spilttedDate = dateString!!.split(" ")
            val startDate = spilttedDate[0]
            val finalStartDate = "$startDate 00:00:01"
            val finalEndDate = "$startDate 23:59:59"
            if (firebaseAuth.currentUser != null) {
                firestore = FirebaseFirestore.getInstance()
                val nextQuery = firestore.collection("Posts").orderBy("date_time")
                    .whereGreaterThanOrEqualTo("date_time", finalStartDate)
                    .whereLessThanOrEqualTo("date_time", finalEndDate)
                nextQuery.addSnapshotListener { documentSnapshots, e ->
                    if (documentSnapshots != null) {
                        if (!documentSnapshots.isEmpty) {
                            for (doc in documentSnapshots.documentChanges) {
                                if (doc.type === DocumentChange.Type.ADDED) {
                                    val postId = doc.document.id
                                    val pojo = doc.document.toObject(PostsModel::class.java).withId<PostsModel>(postId)
                                    posts_list.add(pojo)
                                    fullPage.adapter?.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadLeaderboardPosts() {
        val dateString = AppPreferences(context!!).getCurrentDate()
        val spilttedDate = dateString!!.split(" ")
        val startDate = spilttedDate[0]
        val finalStartDate = "$startDate 00:00:01"
        val finalEndDate = "$startDate 23:59:59"
        if (firebaseAuth.currentUser != null) {

            firestore = FirebaseFirestore.getInstance()

            var nextQuery = firestore.collection("Posts")
                .orderBy("likes_count", Query.Direction.DESCENDING)
                .whereEqualTo("likes_count", 0)
                .whereEqualTo("date_time",finalStartDate)
                .whereEqualTo("date_time",finalEndDate)
                .limit(10)

            nextQuery.addSnapshotListener { documentSnapshots, _ ->
                if (documentSnapshots != null) {
                    if (!documentSnapshots.isEmpty) {
                        for (doc in documentSnapshots.documentChanges) {
                            if (doc.type === DocumentChange.Type.ADDED) {
                                val PostId = doc.document.id
                                val pojo = doc.document.toObject(PostsModel::class.java).withId<PostsModel>(PostId)
                                arrayList.add(pojo)
                                for (i in arrayList.indices){
                                    arrayList[i].date_time
                                }
                                leaderboard_list.add(pojo)
                                leaderBoard.adapter?.notifyDataSetChanged()
                            }

                        }
                    }
                }
            }

        }

    }
}
