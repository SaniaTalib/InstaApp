package com.alidevs.instaapp.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alidevs.instaapp.R
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import kotlinx.android.synthetic.main.activity_main.*

@GlideModule
class App : AppGlideModule()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
