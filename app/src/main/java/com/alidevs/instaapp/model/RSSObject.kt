package com.alidevs.instaapp.model

import com.google.gson.annotations.Expose

data class RSSObject(@Expose val status:String, @Expose val feed:Feed, @Expose val items:List<Item>)