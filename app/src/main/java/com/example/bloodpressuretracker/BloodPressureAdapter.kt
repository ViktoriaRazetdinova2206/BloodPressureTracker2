package com.example.bloodpressuretracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodpressuretracker.databinding.ItemBloodPressureBinding
import java.text.SimpleDateFormat
import java.util.Locale

class BloodPressureAdapter(private val onItemClick: (BloodPressureEntity) -> Unit) :
    ListAdapter<BloodPressureEntity, BloodPressureAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBloodPressureBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemBloodPressureBinding,
        private val onClick: (BloodPressureEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: BloodPressureEntity) {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(record.timestamp)
            binding.tvPressure.text = "${record.systolic}/${record.diastolic}"
            binding.tvPulse.text = "Пульс: ${record.pulse}"
            binding.tvInterpretation.text = interpretPressure(record.systolic, record.diastolic)
            itemView.setOnClickListener { onClick(record) }
        }

        private fun interpretPressure(systolic: Int, diastolic: Int): String {
            return when {
                systolic < 90 && diastolic < 60 -> "Низкое"
                systolic in 90..119 && diastolic in 60..79 -> "Нормальное"
                systolic in 120..129 && diastolic < 80 -> "Повышенное"
                systolic in 130..139 || diastolic in 80..89 -> "Гипертония 1 ст."
                systolic in 140..179 || diastolic in 90..119 -> "Гипертония 2 ст."
                systolic >= 180 || diastolic >= 120 -> "Гипертонический криз"
                else -> "Обратитесь к врачу"
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<BloodPressureEntity>() {
        override fun areItemsTheSame(oldItem: BloodPressureEntity, newItem: BloodPressureEntity) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BloodPressureEntity, newItem: BloodPressureEntity) =
            oldItem == newItem
    }
}