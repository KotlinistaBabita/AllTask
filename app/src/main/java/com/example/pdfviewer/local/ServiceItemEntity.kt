package com.example.pdfviewer.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_items")
data class ServiceItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    @Embedded val data: DataEntity?
)
data class DataEntity(
    val generation: String?,
    val price: String?,
    val capacity: String?,
    val screenSize: String?,
    val description: String?,
    val color: String?,
    val strapColour: String?,
    val caseSize: String?,
    val year: Int?,
    val cpuModel: String?,
    val hardDiskSize: String?,
    val capacityGB: Int?
)
