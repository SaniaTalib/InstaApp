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
import com.alidevs.instaapp.model.Item
import com.alidevs.instaapp.model.RSSObject
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(private val rssObject: RSSObject, var context: Context, private val listener: ItemClickListener) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private lateinit var view: View
    private lateinit var newsImage: ImageView
    private lateinit var newsTitle: TextView


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        init {
            view = itemView.findViewById(R.id.divider)
            newsImage = itemView.findViewById(R.id.news_image)
            newsTitle = itemView.findViewById(R.id.news_title)

            /*itemView.setOnClickListener {
                Log.d("#Trigeradapter","On Click")
                listener.onItemClicked(rssObject.items[adapterPosition],adapterPosition)
            }*/
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

        holder.itemView.setOnClickListener{
            listener.onItemClicked(rssObject.items[position],position)
        }
    }

    override fun getItemCount() = rssObject.items.size

    interface ItemClickListener {
        fun onItemClicked(item: Item, position: Int)
    }
}


/*

fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        val ldf = fragment
        val args = Bundle()
        ldf.arguments = args
        args.putString("url", item)
        ft.replace(R.id.fragment_container, fragment, tag)
        ft.commitAllowingStateLoss()

    }*/
