package com.example.ngsi_image_uploader.retrofit

import com.example.ngsi_image_uploader.Responses.GET_Transaction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiInterface_GET_Transaction {
    @Headers("authorizationToken: " + "fc43478e70344f099a935de0f6778aac")
    @GET("/dev/transactions")
    open fun transactionAPI(): Call<GET_Transaction>?
}