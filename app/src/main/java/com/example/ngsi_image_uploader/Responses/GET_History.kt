package com.example.ngsi_image_uploader.Responses

import com.example.ngsi_image_uploader.Responses.GET_Transaction.Datum

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GET_History {

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
        @SerializedName("full_name")
        @Expose
        private val fullName: String? = null

        @SerializedName("email")
        @Expose
        private val email: String? = null

        @SerializedName("mobile_num")
        @Expose
        private val mobileNum: String? = null

        @SerializedName("cheque_num")
        @Expose
        private val chequeNum: String? = null

        @SerializedName("cheque_image")
        @Expose
        private val chequeImage: String? = null

        @SerializedName("deposit_image")
        @Expose
        private val depositImage: String? = null

        @SerializedName("bank_reference_number")
        @Expose
        private val bankReferenceNumber: String? = null

        @SerializedName("amount")
        @Expose
        private val amount: String? = null

        @SerializedName("transaction_date")
        @Expose
        private val transaction_date: String? = null
    }
}