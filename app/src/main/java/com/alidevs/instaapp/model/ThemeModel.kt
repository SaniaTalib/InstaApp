package com.alidevs.instaapp.model

class ThemeModel: PostID {
    var date: String? = null
    var text: String? = null


    constructor(){}

    constructor(date: String, text: String) {
        this.date = date
        this.text = text
    }

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["date"] = date!!
        result["comments"] = text!!
        return result
    }
}