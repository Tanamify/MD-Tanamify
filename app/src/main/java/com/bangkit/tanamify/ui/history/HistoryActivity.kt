package com.bangkit.tanamify.ui.history

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bangkit.tanamify.databinding.ActivityHistoryBinding
import com.bangkit.tanamify.ui.result.ResultActivity

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }
        val soilClassification = intent.getStringExtra(EXTRA_SOIL_CLASSIFICATION)
        val temperature = intent.getFloatExtra(KEY_TEMPERATURE, 0f)
        val humidity = intent.getFloatExtra(KEY_HUMIDITY, 0f)
        val rainfall = intent.getFloatExtra(KEY_RAIN, 0f)
        val sunlight = intent.getFloatExtra(KEY_SUN, 0f)
        val recommendation = intent.getStringExtra(KEY_RECOMMENDATION)

        imageUri?.let {
            binding.resultImage.setImageURI(it)
            Log.d("ResultActivity", "Received Image URI: $it")
        }

        soilClassification?.let {
            binding.tvResultText.text = it
        }

        binding.tvResultTemp.text = "$temperature °C"
        binding.tvResultHumidity.text = "$humidity %"
        binding.tvResultRain.text = "$rainfall mm"
        binding.tvResultSun.text = "$sunlight jam"

        binding.tvRecommendation.text = "$recommendation"

        binding.btnOkay.setOnClickListener {
            finish()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = "History Hasil Analisis"
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_SOIL_CLASSIFICATION = "extra_soil_classification"
        const val KEY_TEMPERATURE = "temperature"
        const val KEY_HUMIDITY = "humidity"
        const val KEY_RAIN = "rain"
        const val KEY_SUN = "sun"
        const val KEY_RECOMMENDATION = "recommendation"
    }
}