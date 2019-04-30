package com.alidevs.instaapp.model

import com.google.firebase.firestore.Exclude

open class PostID {

    @Exclude
    var PostID: String = ""

    fun <T : PostID> withId(id: String): T {
        this.PostID = id
        return this as T
    }

}