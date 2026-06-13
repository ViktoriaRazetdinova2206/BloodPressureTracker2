package com.example.bloodpressuretracker

import kotlinx.coroutines.flow.Flow

class BloodPressureRepository(private val dao: BloodPressureDao) {
    suspend fun insert(record: BloodPressureEntity) = dao.insert(record)
    suspend fun update(record: BloodPressureEntity) = dao.update(record)
    suspend fun delete(record: BloodPressureEntity) = dao.delete(record)
    fun getAllRecords(): Flow<List<BloodPressureEntity>> = dao.getAllRecords()
    fun getRecordsForChart(): Flow<List<BloodPressureEntity>> = dao.getRecordsForChart()
}