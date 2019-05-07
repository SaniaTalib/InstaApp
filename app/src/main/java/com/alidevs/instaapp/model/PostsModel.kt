package com.alidevs.instaapp.model


import com.google.firebase.firestore.DocumentReference
import java.util.*
import kotlin.collections.HashMap

class PostsModel: PostID {

    var image_thumb: String? = null
    var image_url: String? = null
    var timestamp: Date? = null
    var user_id: String? = null
    var reference: String? = null
    var likes_count: Int? = null
    var date_time: String? = null
    var user_name: String? = null


    constructor() {}

    constructor(image_thumb: String, image_url: String, timestamp: Date, user_id: String, reference: String, likes_count:Int, date_time: String, user_name: String) {
        this.image_thumb = image_thumb
        this.image_url = image_url
        this.timestamp = timestamp
        this.user_id = user_id
        this.reference = reference
        this.likes_count = likes_count
        this.date_time = date_time
        this.user_name = user_name
    }

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("image_thumb", image_thumb!!)
        result.put("image_url", image_url!!)
        result.put("timestamp", timestamp!!)
        result.put("user_id", user_id!!)
        result.put("reference", reference!!)
        result.put("likes_count", likes_count!!)
        result.put("date_time", date_time!!)
        result.put("user_name", user_name!!)
        return result
    }
}