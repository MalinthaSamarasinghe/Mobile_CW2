package com.example.w1832133

import androidx.room.Database
import androidx.room.RoomDatabase

// https://developer.android.com/training/data-storage/room
@Database(entities = [Movie::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}