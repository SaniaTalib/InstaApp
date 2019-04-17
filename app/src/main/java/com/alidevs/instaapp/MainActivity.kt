package com.alidevs.instaapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)


        val actionBar = supportActionBar
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow)
        actionBar!!.title = ""
        actionBar.elevation = 4.0F
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setLogo(R.drawable.ic_logo)
        actionBar.setDisplayUseLogoEnabled(true)

    }


}
