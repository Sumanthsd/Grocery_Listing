package com.example.grocery

import android.app.Application
import androidx.room.Room

class MyApp : Application() {

    lateinit var database: AppDatabase
        private set
    lateinit var repository: GroceryRepository
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "grocery-database"
        ).build()

        repository = GroceryRepository(database.groceryItemDao())
    }
}
