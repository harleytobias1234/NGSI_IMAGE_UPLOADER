package com.example.ngsi_image_uploader.retrofit

import com.example.ngsi_image_uploader.Responses.GET_History
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface_GET_History {
    @Headers("authorizationToken: " + "fc43478e70344f099a935de0f6778aac")
    @GET("/dev/history")
    open fun getHistoryData(
        @Query("email") email: String?,
        @Query("bank_reference_number") bank_reference_number: String?
    ): Call<GET_History?>?
}