package com.alidevs.instaapp.fragment

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.activity.AddWatchesActivity
import com.alidevs.instaapp.activity.LoginActivity
import com.alidevs.instaapp.adapter.MyWatchesAdapter
import com.alidevs.instaapp.model.WatchesModel
import com.alidevs.instaapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyWatchesFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var btnAddWatches: Button
    private var recyclerView: RecyclerView? = null
    val networkAvailability : Boolean
        get() = Utils.isNetworkAvailable(context!!)

    private lateinit var storageReference: StorageReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var user_id: String
    private lateinit var posts_list: MutableList<WatchesModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_my_watches, container, false)
        recyclerView = root.findViewById(R.id.recyclerview)
        btnAddWatches = root.findViewById(R.id.btn_add_watches)
        posts_list = ArrayList()
        //Init components
        if (networkAvailability){
            firebaseAuth = FirebaseAuth.getInstance()
            user_id = firebaseAuth.currentUser!!.uid
            firestore = FirebaseFirestore.getInstance()
            storageReference = FirebaseStorage.getInstance().reference
            loadPosts()
        }else{
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }


        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = context?.let { MyWatchesAdapter(context!!, posts_list) }



        btnAddWatches.setOnClickListener {
            val intent = Intent(context, AddWatchesActivity::class.java)
            startActivity(intent)
        }
        return root
    }


    private fun loadPosts() {
        try {
            if (firebaseAuth.currentUser != null) {
                firestore = FirebaseFirestore.getInstance()
                firestore.collection("MyWatches/$user_id/submissions").addSnapshotListener { documentSnapshots, e ->
                    if (documentSnapshots != null) {
                        if (!documentSnapshots.isEmpty) {
                            for (doc in documentSnapshots.documentChanges) {
                                if (doc.type === DocumentChange.Type.ADDED) {
                                    val postId = doc.document.id
                                    val pojo =
                                        doc.document.toObject(WatchesModel::class.java).withId<WatchesModel>(postId)
                                    posts_list.add(pojo)
                                    recyclerView!!.adapter?.notifyDataSetChanged()
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

    override fun onStart() {
        super.onStart()
        if (networkAvailability){
            val currentUser = firebaseAuth.currentUser?.uid
            if (currentUser == null) {
                sendToLogin()
            }
        }else{
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private fun sendToLogin() {
        val intent = Intent(activity!!, LoginActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }
}
