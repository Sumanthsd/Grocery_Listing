package com.example.grocery

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_items")
data class GroceryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "Item")
    val name: String,

    @ColumnInfo(name = "Star")
    val star: Double,

    @ColumnInfo(name = "DMart")
    val dmart: Double,

    @ColumnInfo(name = "Flipkart")
    val flipkart: Double?,

    @ColumnInfo(name = "BB")
    val bb: Double?,

    @ColumnInfo(name = "QTY")
    val qty: Double
)