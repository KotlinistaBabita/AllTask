package com.example.pdfviewer.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdfviewer.local.ServiceItemEntity
import com.example.pdfviewer.model.Notification
import com.example.pdfviewer.model.NotificationItem
import com.example.pdfviewer.utils.AccessToken
import com.example.pdfviewer.utils.NotificationPrefs
import com.google.firebase.messaging.FirebaseMessaging


import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class ServiceViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _apiItems = MutableLiveData<List<ServiceItemEntity>>()
    val apiItems: LiveData<List<ServiceItemEntity>> get() = _apiItems

    val storedItems: LiveData<List<ServiceItemEntity>> = repository.getAllStoredItems()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchDataFromApi() {
        viewModelScope.launch {
            try {
                val localItems = repository.getAllStoredItemsOnce()
                if (localItems.isEmpty()) {
                    val remoteItems = repository.fetchData()
                    repository.insertItem(remoteItems)
                    _apiItems.postValue(remoteItems)
                } else {
                    _apiItems.postValue(localItems)
                }
            } catch (e: Exception) {
                _error.postValue(e.localizedMessage ?: "An error occurred while fetching data")
            }
        }
    }

    fun insertItem(item: List<ServiceItemEntity>) = viewModelScope.launch {
        repository.insertItem(item)
    }

    fun updateItem(item: ServiceItemEntity) = viewModelScope.launch {
        repository.updateItem(item)
    }



    fun deleteItem(context: Context, item: ServiceItemEntity) = viewModelScope.launch {
        repository.deleteItem(item)

        NotificationPrefs.isNotificationEnabled(context).collect { isEnabled ->
            if (isEnabled) {
                sendNotification(item)
            }
        }
    }

    private fun sendNotification(item: ServiceItemEntity) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCM", "Fetching FCM token failed", task.exception)
                return@addOnCompleteListener
            }

            val deviceToken = task.result

            val accessToken = AccessToken.getToken()
            if (accessToken.isNullOrEmpty()) {
                Log.e("TokenError", "Access token is invalid or empty")
                return@addOnCompleteListener
            }


            val notification = Notification(
                token = deviceToken,
                data = mapOf(
                    "title" to "Deleted Item",
                    "body" to item.name
                )
            )
            val notificationItem = NotificationItem(
                message = notification
            )

            val call = RetrofitClient.secondApiService.sendNotification(
                accessToken = "Bearer $accessToken",
                notificationItem            )

            call.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Log.d("FCM", "Notification sent successfully")
                    } else {
                        Log.e("FCM", "Failed to send notification: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("FCM", "Error sending notification: ${t.message}")
                }
            })
        }
    }

}