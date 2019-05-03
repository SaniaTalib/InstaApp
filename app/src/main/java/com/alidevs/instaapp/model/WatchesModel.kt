package com.alidevs.instaapp.model

import kotlin.collections.HashMap

class WatchesModel: PostID {
    var brand_name: String? = null
    var comments: String? = null
    var image_url_first: String? = null
    var image_url_second: String? = null
    var image_url_third: String? = null
    var image_url_primary: String? = null
    var model: String? = null
    var purchase_date: String? = null
    var reference: String? = null
    var serial: String? = null

    constructor() {}

    constructor(brand_name: String, comments: String, image_url_first: String, image_url_second: String, image_url_third: String,
                image_url_primary:String, model: String, purchase_date: String, reference: String, serial: String) {
        this.brand_name = brand_name
        this.comments = comments
        this.image_url_first = image_url_first
        this.image_url_second = image_url_second
        this.image_url_third = image_url_third
        this.image_url_primary = image_url_primary
        this.model = model
        this.purchase_date = purchase_date
        this.reference = reference
        this.serial = serial
    }

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["brand_name"] = brand_name!!
        result["comments"] = comments!!
        result["image_url_first"] = image_url_first!!
        result["image_url_second"] = image_url_second!!
        result["image_url_third"] = image_url_third!!
        result["image_url_primary"] = image_url_primary!!
        result["model"] = model!!
        result["purchase_date"] = purchase_date!!
        result["reference"] = reference!!
        result["serial"] = serial!!
        return result
    }
}