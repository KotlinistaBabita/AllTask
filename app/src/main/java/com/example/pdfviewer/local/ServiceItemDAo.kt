package com.example.pdfviewer.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ServiceItemDao {
    @Query("SELECT * FROM service_items")
    fun getAllItems(): LiveData<List<ServiceItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ServiceItemEntity>)

    @Query("SELECT * FROM service_items")
    suspend fun getAllItemsOnce(): List<ServiceItemEntity>

    @Update
    suspend fun updateItem(item: ServiceItemEntity)

    @Delete
    suspend fun deleteItem(item: ServiceItemEntity)
}