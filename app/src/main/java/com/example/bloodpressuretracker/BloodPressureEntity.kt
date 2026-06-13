package com.example.bloodpressuretracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "blood_pressure_table")
data class BloodPressureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val systolic: Int,      // верхнее давление
    val diastolic: Int,     // нижнее давление
    val pulse: Int,         // пульс
    val timestamp: Date     // дата и время измерения
)