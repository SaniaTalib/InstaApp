package com.alidevs.instaapp.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.alidevs.instaapp.R
import com.alidevs.instaapp.adapter.FullPageAdapter
import com.alidevs.instaapp.adapter.NewsAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var galleryInactive : ImageView
    private lateinit var galleryActive : ImageView
    private lateinit var pagerActive : ImageView
    private lateinit var pagerInactive : ImageView
    private lateinit var contestInactive : ImageView
    private lateinit var contestActive : ImageView
    private lateinit var recyclerView : RecyclerView
    private lateinit var fullPage : ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        galleryActive = root.findViewById(R.id.gallery_active)
        galleryInactive = root.findViewById(R.id.gallery_inactive)
        pagerActive = root.findViewById(R.id.pager_active)
        pagerInactive = root.findViewById(R.id.pager_inactive)
        contestInactive = root.findViewById(R.id.award_inactive)
        contestActive = root.findViewById(R.id.award_active)
        recyclerView = root.findViewById(R.id.grid_view) as RecyclerView
        fullPage = root.findViewById(R.id.full_page)
        //recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = GridLayoutManager(context,3)
        recyclerView.adapter = context?.let { FullPageAdapter(it) }


        //Click Events
        galleryActive.setOnClickListener {
            galleryActive.visibility = View.GONE
            galleryInactive.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            fullPage.visibility = View.VISIBLE
        }
        galleryInactive.setOnClickListener {
            galleryActive.visibility = View.VISIBLE
            galleryInactive.visibility = View.GONE
            fullPage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        pagerActive.setOnClickListener {
            pagerActive.visibility = View.GONE
            pagerInactive.visibility = View.VISIBLE

        }
        pagerInactive.setOnClickListener {
            pagerActive.visibility = View.VISIBLE
            pagerInactive.visibility = View.GONE
        }
        contestInactive.setOnClickListener {
            contestInactive.visibility = View.GONE
            contestActive.visibility = View.VISIBLE
        }
        contestActive.setOnClickListener {
            contestInactive.visibility = View.VISIBLE
            contestActive.visibility = View.GONE
        }
        return root
    }
}
