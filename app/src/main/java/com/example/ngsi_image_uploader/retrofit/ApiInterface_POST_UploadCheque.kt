package com.example.ngsi_image_uploader.retrofit

import com.example.ngsi_image_uploader.Responses.POST_UploadCheque
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface_POST_UploadCheque {
    @Headers("authorizationToken: " + "fc43478e70344f099a935de0f6778aac")
    @POST("/dev/uploadcheque")
    open fun uploadCheque(@Body credentials: JsonObject?): Call<POST_UploadCheque?>?
    // API's endpoints
}