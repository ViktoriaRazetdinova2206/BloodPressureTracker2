package com.example.bloodpressuretracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BloodPressureEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BloodPressureDatabase : RoomDatabase() {
    abstract fun bloodPressureDao(): BloodPressureDao

    companion object {
        @Volatile
        private var INSTANCE: BloodPressureDatabase? = null

        fun getDatabase(context: Context): BloodPressureDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BloodPressureDatabase::class.java,
                    "blood_pressure_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}