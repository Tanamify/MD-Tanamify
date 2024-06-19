package com.bangkit.tanamify.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.tanamify.R
import com.bangkit.tanamify.data.retrofit.response.HistoryResponse
import com.bangkit.tanamify.databinding.DialogDeleteConfirmationBinding
import com.bangkit.tanamify.databinding.HistoryCardBinding
import com.bangkit.tanamify.ui.history.HistoryActivity
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HistoryAdapter(
    private val deleteHistory: (String) -> Unit
) : ListAdapter<HistoryResponse, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history, deleteHistory)
    }

    class MyViewHolder(private val binding: HistoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(history: HistoryResponse, deleteHistory: (String) -> Unit) {
            val imagePath = "file:///data/user/0/com.bangkit.tanamify/cache/" + history.image
            val imageUri = Uri.parse(imagePath)
            binding.resultImage.setImageURI(imageUri)
            binding.textViewLabel.text = history.soil.substringAfter('-')
            binding.textRecommendation.text = history.result
            binding.textViewTime.text = formatDateTime(history.createdAt)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, HistoryActivity::class.java).apply {
                    putExtra(HistoryActivity.EXTRA_IMAGE_URI, imagePath)
                    putExtra(HistoryActivity.EXTRA_SOIL_CLASSIFICATION, history.soil.substringAfter('-'))
                    putExtra(HistoryActivity.KEY_TEMPERATURE, history.temp)
                    putExtra(HistoryActivity.KEY_HUMIDITY, history.humidity)
                    putExtra(HistoryActivity.KEY_RAIN, history.rain)
                    putExtra(HistoryActivity.KEY_SUN, history.sun)
                    putExtra(HistoryActivity.KEY_RECOMMENDATION, history.result)
                }
                context.startActivity(intent)
            }

            binding.btnDelete.setOnClickListener {
                showDeleteConfirmationDialog(binding.root.context) {
                    deleteHistory(history.idpred)
                }
            }
        }

        private fun showDeleteConfirmationDialog(context: Context, onConfirm: () -> Unit) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_confirmation, null)
            val dialogBinding = DialogDeleteConfirmationBinding.bind(dialogView)

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            dialogBinding.dialogTitle.text = "Konfirmasi Hapus"
            dialogBinding.dialogMessage.text = "Apakah Anda yakin ingin menghapus data ini?"
            dialogBinding.dialogPositive.setOnClickListener {
                onConfirm()
                alertDialog.dismiss()
            }
            dialogBinding.dialogNegative.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun formatDateTime(dateTime: String): String {
            val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val parsedDateTime = ZonedDateTime.parse(dateTime, inputFormatter)
            val jakartaTime = parsedDateTime.withZoneSameInstant(ZoneId.of("Asia/Jakarta"))
            return jakartaTime.format(outputFormatter)
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
