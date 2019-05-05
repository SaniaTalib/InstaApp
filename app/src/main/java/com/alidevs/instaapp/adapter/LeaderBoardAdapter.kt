package com.alidevs.instaapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alidevs.instaapp.R
import com.alidevs.instaapp.model.PostsModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LeaderBoardAdapter(var context: Context, var list: MutableList<PostsModel>) :
    RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {

    private var firebaseFirestore: FirebaseFirestore? = null
    private var firebaseAuth: FirebaseAuth? = null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.img)
        var txtLikes: TextView = itemView.findViewById(R.id.txt_likes)
        var txtRank: TextView = itemView.findViewById(R.id.txt_rank)

        init {
            firebaseFirestore = FirebaseFirestore.getInstance()
            firebaseAuth = FirebaseAuth.getInstance()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_leaderboard2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item: PostsModel = list[position]
        val blogpostID = item.PostID
        val currentUserID = firebaseAuth!!.currentUser!!.uid
        holder.txtRank.text = "# ${position + 1}"


        //Get Likes Count
        firebaseFirestore!!.collection("Posts/$blogpostID/Likes").limit(10)
            .addSnapshotListener { queryDocumentSnapshots, e ->
                if (queryDocumentSnapshots != null) {
                    if (!queryDocumentSnapshots.isEmpty) {
                        val count = queryDocumentSnapshots.size()
                        holder.txtLikes.text = "$count Likes"
                        Log.d("#Count", "$count Likes")
                    } else {
                        holder.txtLikes.text = "0 Likes"
                    }
                }
            }

        Glide.with(holder.itemView.context)
            .load(item.image_url)
            .into(holder.image)
    }

    override fun getItemCount() = list.size
}