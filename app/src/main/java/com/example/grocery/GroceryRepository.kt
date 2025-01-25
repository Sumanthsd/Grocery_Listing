package com.example.grocery

import androidx.lifecycle.LiveData

class GroceryRepository(private val groceryItemDao: GroceryItemDao) {

    val allItems: LiveData<List<GroceryItemEntity>> = groceryItemDao.getAllItems()

    suspend fun insert(groceryItem: GroceryItemEntity) {
        groceryItemDao.insert(groceryItem)
    }
}
