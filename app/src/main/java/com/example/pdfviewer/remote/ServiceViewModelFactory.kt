package com.example.pdfviewer.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ServiceViewModelFactory(private val repository: ServiceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ServiceViewModel(repository) as T
    }
}