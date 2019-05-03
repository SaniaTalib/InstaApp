package com.alidevs.instaapp.adapter

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Message
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.model.PostsModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class FullPageAdapter(var context: Context, var list: MutableList<PostsModel>) :
    RecyclerView.Adapter<FullPageAdapter.ViewHolder>() {

    private val DOUBLE_PRESS_INTERVAL: Long = 250 // in millis
    private var lastPressTime: Long = 0


    private var mHasDoubleClicked = false

    private var firebaseFirestore: FirebaseFirestore? = null
    private var firebaseAuth: FirebaseAuth? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.img)
        var imgHeart: ImageView = itemView.findViewById(R.id.heart)

        init {
            firebaseFirestore = FirebaseFirestore.getInstance()
            firebaseAuth = FirebaseAuth.getInstance()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_full_page, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item: PostsModel = list[position]
        val currentUserId = firebaseAuth!!.currentUser!!.uid
        val blogPostId = item.PostID

        val userID = item.user_id
        Glide.with(holder.itemView.context)
            .load(item.image_url)
            .into(holder.image)

        //Get Likes
        firebaseFirestore!!.collection("Posts/$blogPostId/Likes").document(currentUserId)
            .addSnapshotListener { documentSnapshot, e ->
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.imgHeart.setImageDrawable(context.getDrawable(R.drawable.heart_active))
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.imgHeart.setImageDrawable(context.getDrawable(R.drawable.heart))
                        }
                    }
                }
            }

        holder.image.setOnClickListener {
            val pressTime = System.currentTimeMillis()
            if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL && !mHasDoubleClicked) {
                //Toast.makeText(context, "Double Click Event", Toast.LENGTH_SHORT).show()
                firebaseFirestore!!.collection("Posts/$blogPostId/Likes").document(currentUserId).get()
                    .addOnCompleteListener { task ->
                        if (!task.result!!.exists()) {
                            val likesMap = HashMap<String, Any>()
                            likesMap["timestamp"] = FieldValue.serverTimestamp()
                            firebaseFirestore!!.collection("Posts/$blogPostId/Likes").document(currentUserId)
                                .set(likesMap)

                        } else {
                            firebaseFirestore!!.collection("Posts/$blogPostId/Likes").document(currentUserId).delete()
                        }
                    }

                firebaseFirestore!!.collection("Posts/$blogPostId/Likes")
                    .addSnapshotListener { queryDocumentSnapshots, e ->
                        if (queryDocumentSnapshots != null) {
                            if (!queryDocumentSnapshots.isEmpty) {
                                val count = queryDocumentSnapshots.size()
                                Log.d("#Count", "$count Likes")
                                firebaseFirestore!!.collection("Posts").document(blogPostId)
                                    .update("likes_count", count)
                            } else {
                                firebaseFirestore!!.collection("Posts").document(blogPostId).update("likes_count", 0)
                                Log.d("#Count", "$0 Likes")
                            }
                        }
                    }
                mHasDoubleClicked = true
            } else {
                mHasDoubleClicked = false
                val myHandler = object : Handler() {
                    override fun handleMessage(m: Message) {
                        if (!mHasDoubleClicked) {
                            //Toast.makeText(context, "Single Click Event", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                val m = Message()
                myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL)
            }
            lastPressTime = pressTime
        }
    }

    override fun getItemCount() = list.size
}