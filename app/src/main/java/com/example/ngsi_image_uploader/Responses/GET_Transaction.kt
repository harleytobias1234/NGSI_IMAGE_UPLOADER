package com.example.ngsi_image_uploader.Responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GET_Transaction {
    @SerializedName("statusCode")
    @Expose
    private val statusCode: Int? = null

    @SerializedName("message")
    @Expose
    private val message: String? = null

    @SerializedName("data")
    @Expose
    private val data: ArrayList<Datum>? = null

    class Datum {
        @SerializedName("bank_reference_number")
        @Expose
        private val bankReferenceNumber: String? = null

        @SerializedName("username")
        @Expose
        private val username: String? = null

        @SerializedName("mobile_number")
        @Expose
        private val mobileNumber: String? = null

        @SerializedName("amount")
        @Expose
        private val amount: String? = null

        @SerializedName("status")
        @Expose
        private val status: String? = null
    }
}