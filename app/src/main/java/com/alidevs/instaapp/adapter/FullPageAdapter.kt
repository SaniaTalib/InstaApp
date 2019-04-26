package com.alidevs.instaapp.adapter

import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v7.widget.RecyclerView
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_full_page, parent, false)
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item: PostsModel = list[position]
        val currentUserId = firebaseAuth!!.currentUser!!.uid
        val blogPostId = item.BlogPostId

        val userID = item.user_id
        Glide.with(holder.itemView.context)
            .load(item.image_url)
            .into(holder.image)

        holder.image.setOnClickListener {
            val pressTime = System.currentTimeMillis()
            if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL && !mHasDoubleClicked) {
                Toast.makeText(context, "Double Click Event", Toast.LENGTH_SHORT).show()
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
                mHasDoubleClicked = true
            } else {
                mHasDoubleClicked = false
                val myHandler = object : Handler() {
                    override fun handleMessage(m: Message) {
                        if (!mHasDoubleClicked) {
                            Toast.makeText(context, "Single Click Event", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                val m = Message()
                myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL)
            }
            lastPressTime = pressTime
        }
    }

    /*//Like features
    holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }

    });*/


    override fun getItemCount() = list.size
}