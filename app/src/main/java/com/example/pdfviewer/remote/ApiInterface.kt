package com.example.pdfviewer.remote

import com.example.pdfviewer.local.ServiceItemEntity
import com.example.pdfviewer.model.NotificationItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @GET("objects")
    suspend fun getAllObjects(): List<ServiceItemEntity>

    @POST("v1/projects/my-application-697fb/messages:send")
    @Headers("Content-Type: application/json")
    fun sendNotification(
        @Header("Authorization") accessToken: String,
        @Body body: NotificationItem
    ): Call<ResponseBody>




}