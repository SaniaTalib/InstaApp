package com.alidevs.instaapp.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.activity.LoginActivity
import com.alidevs.instaapp.adapter.FullPageAdapter
import com.alidevs.instaapp.adapter.GridViewAdapter
import com.alidevs.instaapp.adapter.LeaderBoardAdapter
import com.alidevs.instaapp.model.PostsModel
import com.alidevs.instaapp.model.ThemeModel
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), GridViewAdapter.ItemClickListener {

    val TAG = HomeFragment::getTag.toString()
    private lateinit var utils: AppPreferences

    private lateinit var galleryInactive: ImageView
    private lateinit var btnClose: ImageView
    private lateinit var submittedLayout: ConstraintLayout
    private lateinit var uploadImage: ImageView
    private lateinit var pagerInactive: ImageView
    private lateinit var contestInactive: ImageView
    private lateinit var contestActive: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fullPage: RecyclerView
    private lateinit var leaderBoard: RecyclerView
    private lateinit var signupProgress: ProgressBar
    private lateinit var user_id: String
    private lateinit var mainUril: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var compressedImageFile: Bitmap
    private lateinit var posts_list: MutableList<PostsModel>
    private lateinit var theme_list: MutableList<ThemeModel>
    private lateinit var leaderboard_list: MutableList<PostsModel>
    private lateinit var arrayList: ArrayList<PostsModel>
    private lateinit var txtTitle: TextView
    private lateinit var txtTag: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        galleryInactive = root.findViewById(R.id.gallery_inactive)
        pagerInactive = root.findViewById(R.id.pager_inactive)
        contestInactive = root.findViewById(R.id.award_inactive)
        contestActive = root.findViewById(R.id.award_active)
        recyclerView = root.findViewById(R.id.grid_view) as RecyclerView
        fullPage = root.findViewById(R.id.full_page) as RecyclerView
        leaderBoard = root.findViewById(R.id.recycler_leaderboard) as RecyclerView
        uploadImage = root.findViewById(R.id.upload_img)
        signupProgress = root.findViewById(R.id.signup_progress)
        btnClose = root.findViewById(R.id.btn_close)
        submittedLayout = root.findViewById(R.id.submitted_layout)
        txtTitle = root.findViewById(R.id.textView3)
        txtTag = root.findViewById(R.id.textView7)
        posts_list = ArrayList()
        leaderboard_list = ArrayList()
        firebaseAuth = FirebaseAuth.getInstance()
        user_id = firebaseAuth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        utils = AppPreferences(context!!)
        arrayList = ArrayList()
        theme_list = ArrayList()

        loadPosts()
        loadLeaderboardPosts()
        loadTheme()

         if (theme_list.size == 0) {
             txtTag.text = utils.getDate().toString()
         }
        /*******************HOME FRAGMENT**********************/
        val linearSnapHelper = PagerSnapHelper()
        linearSnapHelper.attachToRecyclerView(fullPage)
        val linearSnapHelper1 = PagerSnapHelper()
        linearSnapHelper1.attachToRecyclerView(leaderBoard)
        fullPage.layoutManager = LinearLayoutManager(context)
        fullPage.adapter = context?.let { FullPageAdapter(context!!, posts_list) }
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = context?.let { GridViewAdapter(posts_list, this@HomeFragment) }
        leaderBoard.layoutManager = LinearLayoutManager(context)
        leaderBoard.adapter = context?.let { LeaderBoardAdapter(context!!, leaderboard_list) }

        //Click Events
        uploadImage.setOnClickListener {
            if (utils.getString("isSubmitted") == null || utils.getString("isSubmitted") != utils.getDate()) {
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
            } else {
                submittedLayout.visibility = View.VISIBLE
            }
        }

        btnClose.setOnClickListener {
            submittedLayout.visibility = View.GONE
        }

        galleryInactive.setOnClickListener {
            txtTitle.text = "Gallery"
            pagerInactive.visibility = View.VISIBLE
            contestInactive.visibility = View.VISIBLE
            galleryInactive.visibility = View.GONE
            contestActive.visibility = View.GONE
            fullPage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            leaderBoard.visibility = View.GONE
        }

        pagerInactive.setOnClickListener {
            txtTitle.text = "Gallery"
            galleryInactive.visibility = View.VISIBLE
            contestInactive.visibility = View.VISIBLE
            pagerInactive.visibility = View.GONE
            contestActive.visibility = View.GONE
            fullPage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            leaderBoard.visibility = View.GONE
        }

        contestInactive.setOnClickListener {
            txtTitle.text = "Top 10"
            contestInactive.visibility = View.GONE
            pagerInactive.visibility = View.VISIBLE
            galleryInactive.visibility = View.GONE
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
        var username = ""
        signupProgress.visibility = View.VISIBLE
        val randonName = UUID.randomUUID().toString()
        val newimage = File(mainUril.path)
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
                val newthumbfile = File(mainUril.path)
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
                        val newthumbfile = File(mainUril.path)
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



                        firestore.collection("users").document(user_id).get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    if (document != null) {
                                        username = document.getString("name").toString()
                                    } else {
                                        Log.d("LOGGER", "No such document")
                                    }
                                } else {
                                    Log.d("LOGGER", "get failed with ", task.exception);
                                }
                            }



                        uploadTask.addOnFailureListener {
                            Toast.makeText(this.activity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener {
                            val downloadthumburl = downloadurl.result
                            val items = HashMap<String, Any>()
                            val dateString = AppPreferences(context!!).getCurrentDate()
                            items["image_url"] = downloadurl.result.toString()
                            items["image_thumb"] = downloadthumburl.toString()
                            items["reference"] = downloadthumburl.toString()
                            items["date_time"] = dateString.toString()
                            items["timestamp"] = FieldValue.serverTimestamp()
                            items["likes_count"] = 0
                            items["user_name"] = username

                            firestore.collection("Posts").add(items)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        signupProgress.visibility = View.GONE
                                        utils.putString("isSubmitted", utils.getDate().toString())
                                        Toast.makeText(context, "Post was added", Toast.LENGTH_LONG).show()
                                    }
                                }.addOnFailureListener {
                                    signupProgress.visibility = View.GONE
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
        val currentUser = firebaseAuth.currentUser?.uid
        if (currentUser == null) {
            sendToLogin()
        } else {
            firestore.collection("users").document(currentUser).update("lastactive", FieldValue.serverTimestamp())
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
                .whereGreaterThan("likes_count", 0).limit(10)

            nextQuery.addSnapshotListener { documentSnapshots, _ ->
                if (documentSnapshots != null) {
                    if (!documentSnapshots.isEmpty) {
                        for (doc in documentSnapshots.documentChanges) {
                            if (doc.type === DocumentChange.Type.ADDED) {
                                val PostId = doc.document.id
                                val pojo = doc.document.toObject(PostsModel::class.java).withId<PostsModel>(PostId)
                                arrayList.add(pojo)

                                val sdf = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", Locale.US)
                                val date1 = sdf.parse(finalStartDate)
                                val date2 = sdf.parse(finalEndDate)
                                val currentDate = sdf.parse(arrayList[doc.newIndex].date_time)
                                if (currentDate.after(date1) && currentDate.before(date2)) {
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

    private fun loadTheme() {
        try {
            val dateString = utils.getDate1()
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            var date1 = sdf.parse(dateString).toString()
            if (firebaseAuth.currentUser != null) {

                firestore = FirebaseFirestore.getInstance()

                var nextQuery = firestore.collection("Theme")

                nextQuery.addSnapshotListener { documentSnapshots, _ ->
                    if (documentSnapshots != null) {
                        if (!documentSnapshots.isEmpty) {
                            for (doc in documentSnapshots.documentChanges) {
                                if (doc.type === DocumentChange.Type.ADDED) {
                                    val PostId = doc.document.id
                                    val pojo = doc.document.toObject(ThemeModel::class.java).withId<ThemeModel>(PostId)

                                    if (pojo.date == utils.getDate1()) {
                                        theme_list.add(pojo)
                                        txtTag.text = "#${theme_list[theme_list.size - 1].text}"
                                        /*theme_list.add(pojo)*/
                                    }

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

    override fun onItemClicked(item: PostsModel, position: Int) {
        fullPage.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        fullPage.smoothScrollToPosition(position)
        galleryInactive.visibility = View.VISIBLE
        pagerInactive.visibility = View.GONE
    }


}
