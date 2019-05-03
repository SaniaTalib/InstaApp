package com.alidevs.instaapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alidevs.instaapp.R
import com.alidevs.instaapp.model.WatchesModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.news_item.view.*

class MyWatchesAdapter(var context: Context, var list: MutableList<WatchesModel>) :
    RecyclerView.Adapter<MyWatchesAdapter.ViewHolder>() {

    private lateinit var view: View

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageView2)
        var txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        var txtref: TextView = itemView.findViewById(R.id.txt_ref)
        var txtserial: TextView = itemView.findViewById(R.id.txt_serial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mywatches, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        val item: WatchesModel = list[position]
        holder.txtTitle.text = item.brand_name
        holder.txtserial.text = item.serial
        holder.txtref.text = item.reference


        Glide.with(holder.itemView.context)
            .load(item.image_url_primary)
            .into(holder.image)

        //Hide Last item divider
        if (position == list.size - 1) {
            holder.itemView.divider.visibility = View.GONE
        }
    }

    override fun getItemCount() = list.size
}