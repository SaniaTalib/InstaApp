package com.alidevs.instaapp.model

import com.google.firebase.firestore.Exclude

open class PostID {

    @Exclude
    var BlogPostId: String = ""

    fun <T : PostID> withId(id: String): T {
        this.BlogPostId = id
        return this as T
    }

}