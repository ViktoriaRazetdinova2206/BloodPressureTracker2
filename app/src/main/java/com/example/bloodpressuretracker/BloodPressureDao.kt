package com.example.bloodpressuretracker

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BloodPressureDao {
    @Insert
    suspend fun insert(record: BloodPressureEntity)

    @Update
    suspend fun update(record: BloodPressureEntity)

    @Delete
    suspend fun delete(record: BloodPressureEntity)

    @Query("SELECT * FROM blood_pressure_table ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<BloodPressureEntity>>

    @Query("SELECT * FROM blood_pressure_table ORDER BY timestamp ASC")
    fun getRecordsForChart(): Flow<List<BloodPressureEntity>>
}