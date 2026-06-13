package com.example.bloodpressuretracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BloodPressureViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BloodPressureRepository
    val allRecords: LiveData<List<BloodPressureEntity>>
    val chartRecords: LiveData<List<BloodPressureEntity>>

    init {
        val database = BloodPressureDatabase.getDatabase(application)
        repository = BloodPressureRepository(database.bloodPressureDao())
        // Преобразуем Flow в LiveData
        allRecords = repository.getAllRecords().asLiveData()
        chartRecords = repository.getRecordsForChart().asLiveData()
    }

    fun insert(record: BloodPressureEntity) = viewModelScope.launch {
        repository.insert(record)
    }

    fun update(record: BloodPressureEntity) = viewModelScope.launch {
        repository.update(record)
    }

    fun delete(record: BloodPressureEntity) = viewModelScope.launch {
        repository.delete(record)
    }
}