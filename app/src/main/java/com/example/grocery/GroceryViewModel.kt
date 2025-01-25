package com.example.grocery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GroceryViewModel(private val repository: GroceryRepository) : ViewModel() {

    val allItems: LiveData<List<GroceryItemEntity>> = repository.allItems

    fun insert(groceryItem: GroceryItemEntity) {
        viewModelScope.launch {
            repository.insert(groceryItem)
        }
    }
}
