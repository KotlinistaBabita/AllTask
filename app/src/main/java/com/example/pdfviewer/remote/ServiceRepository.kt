package com.example.pdfviewer.remote

import androidx.lifecycle.LiveData
import com.example.pdfviewer.local.ServiceItemDao
import com.example.pdfviewer.local.ServiceItemEntity

class ServiceRepository(
    private val apiService: ApiInterface,
    private val serviceDao: ServiceItemDao
) {

    fun getAllStoredItems(): LiveData<List<ServiceItemEntity>> = serviceDao.getAllItems()

    suspend fun getAllStoredItemsOnce(): List<ServiceItemEntity> = serviceDao.getAllItemsOnce()

    suspend fun fetchData(): List<ServiceItemEntity> {
        return apiService.getAllObjects()
    }
    // Local
    suspend fun insertItem(items: List<ServiceItemEntity>) {
        try {
            serviceDao.insertItems(items)
        } catch (e: Exception) {
            throw Exception("Database insert failed: ${e.message}", e)
        }
    }
    suspend fun updateItem(item: ServiceItemEntity) = serviceDao.updateItem(item)
    suspend fun deleteItem(item: ServiceItemEntity) = serviceDao.deleteItem(item)
}