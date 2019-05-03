package com.alidevs.instaapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alidevs.instaapp.R
import com.alidevs.instaapp.activity.WatchesDetailActivity
import com.alidevs.instaapp.model.WatchesModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.news_item.view.*

class MyWatchesAdapter(var context: Context, var list: MutableList<WatchesModel>) :
    RecyclerView.Adapter<MyWatchesAdapter.ViewHolder>() {

    private lateinit var view: View

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageView2)
        var txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        var txtref: TextView = itemView.findViewById(R.id.title_serial)
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


        holder.itemView.setOnClickListener {
            val intent = Intent(context, WatchesDetailActivity::class.java)
            intent.putExtra("reference", item.reference)
            intent.putExtra("serial", item.serial)
            intent.putExtra("pdate", item.purchase_date)
            intent.putExtra("comments", item.comments)
            intent.putExtra("primary_img", item.image_url_primary)
            intent.putExtra("first_img", item.image_url_first)
            intent.putExtra("second_img", item.image_url_second)
            intent.putExtra("third_img", item.image_url_third)
            context.startActivity(intent)
        }

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