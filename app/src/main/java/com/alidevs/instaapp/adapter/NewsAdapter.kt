package com.alidevs.instaapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alidevs.instaapp.R
import com.alidevs.instaapp.model.RSSObject
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(private val rssObject: RSSObject, var context: Context) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private lateinit var view: View
    private lateinit var newsImage: ImageView
    private lateinit var newsTitle: TextView


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        init {
            view = itemView.findViewById(R.id.divider)
            newsImage = itemView.findViewById(R.id.news_image)
            newsTitle = itemView.findViewById(R.id.news_title)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = rssObject.items[position]

        //Split String from RSS to get valid image URL
        val imgString = item.thumbnail
        var imgSplit = imgString.split("?")
        val finalImage = imgSplit[0]

        //Load Image URL to Imageview and Data to views
        newsTitle.text = item.title

        Glide.with(holder.itemView.context)
            .load(finalImage)
            .into(newsImage)


        //Hide Last item divider
        if (position==rssObject.items.size-1){
            holder.itemView.divider.visibility = View.GONE
        }
    }

    override fun getItemCount() = rssObject.items.size
}