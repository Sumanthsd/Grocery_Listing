package com.example.grocery

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GroceryItemDao {

    @Insert
    suspend fun insert(groceryItem: GroceryItemEntity)

    @Query("SELECT * FROM grocery_items")
    fun getAllItems(): LiveData<List<GroceryItemEntity>>
}
