package com.example.bloodpressuretracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.bloodpressuretracker.databinding.ActivityReminderBinding
import java.util.concurrent.TimeUnit

class ReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Создаём канал уведомлений  — для Android 8+
        createNotificationChannel()

        binding.btnSetReminder.setOnClickListener {
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            scheduleDailyReminder(hour, minute)
        }

        binding.btnCancelReminder.setOnClickListener {
            cancelReminder()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Напоминания о давлении",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Канал для ежедневных напоминаний"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleDailyReminder(hour: Int, minute: Int) {
        // Вычисляем время первого запуска (сегодня в HH:MM, или завтра если уже прошло)
        val now = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, hour)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
            if (timeInMillis <= now) {
                add(java.util.Calendar.DAY_OF_MONTH, 1)
            }
        }
        val initialDelay = calendar.timeInMillis - now

        // Создаём периодическую работу (каждые 24 часа)
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )

        Toast.makeText(this, "Напоминание установлено на $hour:$minute", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun cancelReminder() {
        WorkManager.getInstance(this).cancelUniqueWork("daily_reminder")
        Toast.makeText(this, "Напоминания отключены", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}