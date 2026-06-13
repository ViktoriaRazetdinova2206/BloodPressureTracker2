package com.example.bloodpressuretracker

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import com.example.bloodpressuretracker.databinding.ActivityMainBinding
import com.example.bloodpressuretracker.databinding.DialogAddPressureBinding
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: BloodPressureViewModel by viewModels()
    private lateinit var adapter: BloodPressureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Запрос разрешения на уведомления для Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> binding.drawerLayout.closeDrawers()
                R.id.nav_chart -> {
                    startActivity(Intent(this, ChartActivity::class.java))
                    binding.drawerLayout.closeDrawers()
                }
                R.id.nav_reminder -> {
                    startActivity(Intent(this, ReminderActivity::class.java))
                    binding.drawerLayout.closeDrawers()
                }
            }
            true
        }

        adapter = BloodPressureAdapter { record ->
            showEditDeleteDialog(record)
        }
        binding.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.allRecords.observe(this) { records ->
            adapter.submitList(records)
        }

        binding.fabAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialogView = DialogAddPressureBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle("Новая запись")
            .setView(dialogView.root)
            .setPositiveButton("Сохранить") { _, _ ->
                val systolic = dialogView.etSystolic.text.toString().toIntOrNull()
                val diastolic = dialogView.etDiastolic.text.toString().toIntOrNull()
                val pulse = dialogView.etPulse.text.toString().toIntOrNull()
                if (systolic != null && diastolic != null && pulse != null) {
                    val record = BloodPressureEntity(
                        systolic = systolic,
                        diastolic = diastolic,
                        pulse = pulse,
                        timestamp = Date()
                    )
                    viewModel.insert(record)
                    Toast.makeText(this, "Запись добавлена", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showEditDeleteDialog(record: BloodPressureEntity) {
        AlertDialog.Builder(this)
            .setTitle("Действие")
            .setItems(arrayOf("Редактировать", "Удалить")) { _, which ->
                when (which) {
                    0 -> showEditDialog(record)
                    1 -> {
                        viewModel.delete(record)
                        Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .show()
    }

    private fun showEditDialog(record: BloodPressureEntity) {
        val dialogView = DialogAddPressureBinding.inflate(layoutInflater)
        dialogView.etSystolic.setText(record.systolic.toString())
        dialogView.etDiastolic.setText(record.diastolic.toString())
        dialogView.etPulse.setText(record.pulse.toString())
        AlertDialog.Builder(this)
            .setTitle("Редактировать")
            .setView(dialogView.root)
            .setPositiveButton("Обновить") { _, _ ->
                val systolic = dialogView.etSystolic.text.toString().toIntOrNull()
                val diastolic = dialogView.etDiastolic.text.toString().toIntOrNull()
                val pulse = dialogView.etPulse.text.toString().toIntOrNull()
                if (systolic != null && diastolic != null && pulse != null) {
                    val updatedRecord = record.copy(
                        systolic = systolic,
                        diastolic = diastolic,
                        pulse = pulse,
                        timestamp = Date()
                    )
                    viewModel.update(updatedRecord)
                    Toast.makeText(this, "Запись обновлена", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(binding.navView)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}