package com.alidevs.instaapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alidevs.instaapp.R
import kotlinx.android.synthetic.main.news_item.view.*

class MyWatchesAdapter(var context: Context) : RecyclerView.Adapter<MyWatchesAdapter.ViewHolder>() {

    private lateinit var view: View

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        init {
            view = itemView.findViewById(R.id.divider)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mywatches, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        if (position==1){
            holder.itemView.divider.visibility = View.GONE
        }
    }

    override fun getItemCount() = 2
}