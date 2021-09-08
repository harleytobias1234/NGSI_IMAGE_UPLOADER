package com.example.ngsi_image_uploader.retrofit.adapter

import com.example.ngsi_image_uploader.retrofit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object Api_NGSI {
    private val BASE_URL = "https://wkjvdbdpo2.execute-api.ap-east-1.amazonaws.com"
    private var retrofit: Retrofit? = null

    fun postRegistration(): ApiInterface_POST_Registration? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        //Creating object for our interface
        return retrofit!!.create(ApiInterface_POST_Registration::class.java) // return the APIInterface object
    }
    fun postUploadCheque(): ApiInterface_POST_UploadCheque? {
//        val client: OkHttpClient = OkHttpClient.Builder()
//            .connectTimeout(30000, TimeUnit.SECONDS)
//            .readTimeout(30000, TimeUnit.SECONDS).build()
        // change your base URL
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        //Creating object for our interface
        return retrofit!!.create(ApiInterface_POST_UploadCheque::class.java) // return the APIInterface object
    }
    fun postLoginAPI(): ApiInterface_POST_Login? {
//        ///Status Checkers
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
//        //////
        // change your base URL
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        //Creating object for our interface
        return retrofit!!.create(ApiInterface_POST_Login::class.java) // return the APIInterface object
    }
    fun getTransactionAPI(): ApiInterface_GET_Transaction? {
        ///Status Checkers
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        //////
        // change your base URL
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        //Creating object for our interface
        return retrofit!!.create(ApiInterface_GET_Transaction::class.java) // return the APIInterface object
    }
    fun getHistoryAPI(): ApiInterface_GET_History? {
        ///Status Checkers
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        //////
        // change your base URL
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        //Creating object for our interface
        return retrofit!!.create(ApiInterface_GET_History::class.java) // return the APIInterface object
    }
    //
}

