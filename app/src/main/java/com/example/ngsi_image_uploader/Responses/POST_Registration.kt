package com.example.ngsi_image_uploader.Responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class POST_Registration {
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int? = null

    @SerializedName("error")
    @Expose
    val error: String? = null

    @SerializedName("message")
    @Expose
    val message: String? = null
}