package com.example.grocery

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [GroceryItemEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groceryItemDao(): GroceryItemDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create a new table with the changes
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS grocery_item_new (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "name TEXT NOT NULL, " +
                            "star REAL NOT NULL, " +
                            "dmart REAL NOT NULL, " +
                            "flipkart REAL, " +
                            "bb REAL, " +
                            "Qty REAL NOT NULL)"
                )

                // Copy data from the old table to the new one
                database.execSQL("INSERT INTO grocery_item_new (id, name, star, dmart, flipkart, bb, Qty) SELECT id, name, star, dmart, flipkart, bb, Qty FROM grocery_item")

                // Remove the old table
                database.execSQL("DROP TABLE IF EXISTS grocery_item")

                // Rename the new table to the original table name
                database.execSQL("ALTER TABLE grocery_item_new RENAME TO grocery_item")
            }
        }
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration() // Use destructive migration
                    .addMigrations(migration1to2) // Apply the migration
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
