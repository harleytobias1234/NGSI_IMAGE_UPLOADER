package com.example.ngsi_image_uploader.Responses

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class POST_UploadCheque {
    @SerializedName("statusCode")
    @Expose
    private val statusCode: Int? = null

    @SerializedName("error")
    @Expose
    private val error: String? = null

    @SerializedName("message")
    @Expose
    private val message: String? = null

}