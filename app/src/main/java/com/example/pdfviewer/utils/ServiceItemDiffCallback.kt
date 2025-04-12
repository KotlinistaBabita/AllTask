package com.example.pdfviewer.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.pdfviewer.local.ServiceItemEntity

object ServiceItemDiffCallback : DiffUtil.ItemCallback<ServiceItemEntity>() {
    override fun areItemsTheSame(oldItem: ServiceItemEntity, newItem: ServiceItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ServiceItemEntity, newItem: ServiceItemEntity): Boolean {
        return oldItem == newItem
    }
}