package com.example.ngsi_image_uploader.retrofit

import com.example.ngsi_image_uploader.Responses.POST_Registration
import retrofit2.http.Headers
import retrofit2.http.POST
import com.google.gson.JsonObject
import retrofit2.Call

import retrofit2.http.Body

interface ApiInterface_POST_Registration {
    @Headers("authorizationToken: " + "fc43478e70344f099a935de0f6778aac")
    @POST("/dev/registration")
    open fun registration(@Body credentials: JsonObject?): Call<POST_Registration?>?
    // API's endpoints
}