package com.bangkit.tanamify.utils

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.tanamify.data.local.HistoryEntity
import com.bangkit.tanamify.databinding.HistoryCardBinding
import com.bangkit.tanamify.ui.result.ResultActivity


class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    class MyViewHolder(private val binding: HistoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: HistoryEntity) {
            val imageUri = Uri.parse(history.uri)
            binding.resultImage.setImageURI(imageUri)
            binding.textViewLabel.text = history.result
            binding.textRecommendation.text = history.recommendation
            binding.textViewTime.text = history.date

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, ResultActivity::class.java).apply {
                    putExtra(ResultActivity.EXTRA_IMAGE_URI, history.uri)
                    putExtra(ResultActivity.EXTRA_SOIL_CLASSIFICATION, history.result)
                    putExtra(ResultActivity.KEY_TEMPERATURE, history.temperature)
                    putExtra(ResultActivity.KEY_HUMIDITY, history.humidity)
                    putExtra(ResultActivity.KEY_RAIN, history.rain)
                    putExtra(ResultActivity.KEY_SUN, history.sun)
                    putExtra(ResultActivity.KEY_RECOMMENDATION, history.recommendation)

                }
                context.startActivity(intent)
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}