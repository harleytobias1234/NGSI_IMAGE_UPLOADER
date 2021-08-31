package com.example.ngsi_image_uploader.Responses

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class POST_Login {
    @SerializedName("statusCode")
    @Expose
    private val statusCode: Int? = null

    @SerializedName("message")
    @Expose
    private val message: String? = null

    @SerializedName("data")
    @Expose
    private val data: Data? = null

    class Data {

        @SerializedName("user_id")
        @Expose
        private val userId: String? = null

        @SerializedName("full_name")
        @Expose
        private val fullName: String? = null

        @SerializedName("email")
        @Expose
        private val email: String? = null
    }

}

