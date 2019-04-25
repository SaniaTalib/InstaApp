package com.alidevs.instaapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alidevs.instaapp.R
import com.alidevs.instaapp.model.PostsModel
import com.bumptech.glide.Glide

class GridViewAdapter(var list:MutableList<PostsModel>) : RecyclerView.Adapter<GridViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image : ImageView = itemView.findViewById(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gridview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.setIsRecyclable(false)
        val item : PostsModel = list[position]

        val userID = item.user_id
        Glide.with(holder.itemView.context)
            .load(item.image_url)
            .into(holder.image)
    }

    override fun getItemCount() = list.size
}