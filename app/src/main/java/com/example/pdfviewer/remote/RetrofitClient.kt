package com.example.pdfviewer.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.restful-api.dev/"
    private const val FCM_URL = "https://fcm.googleapis.com"

    val apiService: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    val secondApiService: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(FCM_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}