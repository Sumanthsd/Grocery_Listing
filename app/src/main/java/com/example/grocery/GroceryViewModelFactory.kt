package com.example.grocery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroceryViewModelFactory(private val repository: GroceryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(GroceryViewModel::class.java)) {
            "Unknown ViewModel class: ${modelClass.name}"
        }
        return GroceryViewModel(repository) as T
    }
}