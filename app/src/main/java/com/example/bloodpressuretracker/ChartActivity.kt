package com.example.bloodpressuretracker

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bloodpressuretracker.databinding.ActivityChartBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartBinding
    private val viewModel: BloodPressureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.chartRecords.observe(this) { records ->
            val systolicEntries = mutableListOf<Entry>()
            val diastolicEntries = mutableListOf<Entry>()
            for ((index, record) in records.withIndex()) {
                systolicEntries.add(Entry(index.toFloat(), record.systolic.toFloat()))
                diastolicEntries.add(Entry(index.toFloat(), record.diastolic.toFloat()))
            }
            val systolicDataSet = LineDataSet(systolicEntries, "Систолическое").apply {
                color = Color.BLUE
                setCircleColor(Color.BLUE)
                lineWidth = 2f
                circleRadius = 4f
            }
            val diastolicDataSet = LineDataSet(diastolicEntries, "Диастолическое").apply {
                color = Color.RED
                setCircleColor(Color.RED)
                lineWidth = 2f
                circleRadius = 4f
            }
            binding.lineChart.data = LineData(systolicDataSet, diastolicDataSet)
            binding.lineChart.invalidate()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        fun newIntent(context: android.content.Context) = android.content.Intent(context, ChartActivity::class.java)
    }
}