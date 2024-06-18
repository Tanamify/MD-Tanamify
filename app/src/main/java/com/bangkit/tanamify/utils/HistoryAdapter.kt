package com.bangkit.tanamify.utils

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.tanamify.data.retrofit.response.HistoryResponse
import com.bangkit.tanamify.databinding.HistoryCardBinding
import com.bangkit.tanamify.ui.history.HistoryActivity

class HistoryAdapter(
    private val deleteHistory: (String) -> Unit
) : ListAdapter<HistoryResponse, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history, deleteHistory)
    }

    class MyViewHolder(val binding: HistoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: HistoryResponse, deleteHistory: (String) -> Unit) {
            val imagePath = "file:///data/user/0/com.bangkit.tanamify/cache/" + history.image
            val imageUri = Uri.parse(imagePath)
            binding.resultImage.setImageURI(imageUri)
            binding.textViewLabel.text = history.soil
            binding.textRecommendation.text = history.result
            binding.textViewTime.text = history.createdAt

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, HistoryActivity::class.java).apply {
                    putExtra(HistoryActivity.EXTRA_IMAGE_URI, imagePath)
                    putExtra(HistoryActivity.EXTRA_SOIL_CLASSIFICATION, history.soil)
                    putExtra(HistoryActivity.KEY_TEMPERATURE, history.temp)
                    putExtra(HistoryActivity.KEY_HUMIDITY, history.humidity)
                    putExtra(HistoryActivity.KEY_RAIN, history.rain)
                    putExtra(HistoryActivity.KEY_SUN, history.sun)
                    putExtra(HistoryActivity.KEY_RECOMMENDATION, history.result)
                }
                context.startActivity(intent)
            }

            binding.btnDelete.setOnClickListener {
                deleteHistory(history.idpred)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryResponse>() {
            override fun areItemsTheSame(oldItem: HistoryResponse, newItem: HistoryResponse): Boolean {
                return oldItem.idpred == newItem.idpred
            }

            override fun areContentsTheSame(oldItem: HistoryResponse, newItem: HistoryResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}
