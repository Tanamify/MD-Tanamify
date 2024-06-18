package com.bangkit.tanamify.ui.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.tanamify.MainActivity
import com.bangkit.tanamify.databinding.ActivityResultBinding
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.bangkit.tanamify.data.local.HistoryEntity
import com.bangkit.tanamify.data.local.TanamifyDatabase

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var tflite: Interpreter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        try {
            tflite = Interpreter(loadModelFile("ANN_MODEL.tflite"))
        } catch (e: Exception) {
            showToast("Gagal memuat model TFLite: ${e.message}")
        }

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val soilClassification = intent.getStringExtra(EXTRA_SOIL_CLASSIFICATION)
        val temperature = intent.getFloatExtra(KEY_TEMPERATURE, 0f)
        val humidity = intent.getFloatExtra(KEY_HUMIDITY, 0f)
        val rainfall = intent.getFloatExtra(KEY_RAIN, 0f)
        val sunlight = intent.getFloatExtra(KEY_SUN, 0f)

        val soilTypeNumeric = convertSoilClassificationToFloat(soilClassification)
        val inputArray = floatArrayOf(temperature, humidity, rainfall, sunlight, soilTypeNumeric)

        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }

        soilClassification?.let {
            binding.tvResultText.text = it
        }

        binding.tvResultTemp.text = "$temperature °C"
        binding.tvResultHumidity.text = "$humidity %"
        binding.tvResultRain.text = "$rainfall mm"
        binding.tvResultSun.text = "$sunlight jam"

        Log.d("ResultActivity", "Temperature: $temperature °C")
        Log.d("ResultActivity", "Humidity: $humidity %")
        Log.d("ResultActivity", "Rainfall: $rainfall mm")
        Log.d("ResultActivity", "Sunlight: $sunlight jam")
        Log.d("ResultActivity", "Jenis Tanah: $soilTypeNumeric")

        showRecommendation(inputArray)

        binding.btnOkay.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = "Hasil Analisis"
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showRecommendation(inputArray: FloatArray) {
        val recommendations = runAnnModel(inputArray)
        binding.tvRecommendation.text = recommendations

        val soilClassification = binding.tvResultText.text.toString()
        val temperature = inputArray[0]
        val humidity = inputArray[1]
        val rain = inputArray[2]
        val sun = inputArray[3]
        val soil = inputArray[4]

        Log.d("ResultActivity", "Temperature: $temperature °C")
        Log.d("ResultActivity", "Humidity: $humidity %")
        Log.d("ResultActivity", "Rainfall: $rain mm")
        Log.d("ResultActivity", "Sunlight: $sun jam")
        Log.d("ResultActivity", "SoilType: $soil")

        saveResultToHistory(
            soilClassification,
            temperature,
            humidity,
            rain,
            sun,
            recommendations,
            intent.getStringExtra(EXTRA_IMAGE_URI) ?: ""
        )
    }

    private fun runAnnModel(inputArray: FloatArray): String {
        if (tflite == null) {
            showToast("Interpreter TFLite tidak terinisialisasi")
            return "Unknown"
        }

        return try {
            Log.d("ResultActivity", "Input Array (before model): ${inputArray.joinToString(", ")}")

            val input = arrayOf(inputArray)
            val output = Array(1) { FloatArray(NUM_OUTPUT_CLASSES) }

            tflite!!.run(input, output)

            Log.d("OutputArray", "Content: ${output[0].joinToString(", ")}")

            val recommendation = getRecommendationLabel(output[0])

            recommendation
        } catch (e: Exception) {
            showToast("Gagal menjalankan model ANN: ${e.message}")
            "Unknown"
        }
    }

    private fun getRecommendationLabel(outputArray: FloatArray): String {
        return when (outputArray.indices.maxByOrNull { outputArray[it] } ?: -1) {
            0 -> "Jagung"
            1 -> "Kedelai"
            2 -> "Padi Gogo"
            3 -> "Padi Sawah"
            4 -> "Ubi Jalar"
            5 -> "Ubi Kayu"
            else -> "Unknown"
        }
    }

    private fun loadModelFile(fileName: String): MappedByteBuffer {
        val fileDescriptor = assets.openFd(fileName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveResultToHistory(result: String, temperature: Float, humidity: Float, rain: Float, sun: Float, recommendation: String, uri: String) {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val historyEntity = HistoryEntity(
            date = date,
            result = result,
            temperature = temperature,
            humidity = humidity,
            rain = rain,
            sun = sun,
            recommendation = recommendation,
            uri = uri
        )

        lifecycleScope.launch {
            val db = TanamifyDatabase.getDatabase(applicationContext)
            db.historyDao().addHistory(historyEntity)
        }
    }

    private fun convertSoilClassificationToFloat(soilClassification: String?): Float {
        return when (soilClassification?.trim()) {
            "01-Aluvial" -> 1f
            "02-Andosol" -> 2f
            "03-Entisol" -> 3f
            "04-Humus" -> 4f
            "05-Inceptisol" -> 5f
            "06-Laterit" -> 6f
            "07-Kapur" -> 7f
            "08-Pasir" -> 8f
            else -> {
                Log.e("ResultActivity", "Unknown soil classification: $soilClassification")
                0f
            }
        }
    }

    override fun onDestroy() {
        tflite?.close()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_SOIL_CLASSIFICATION = "extra_soil_classification"
        const val EXTRA_INPUT_ARRAY = "extra_input_array"
        const val NUM_OUTPUT_CLASSES = 6

        const val KEY_TEMPERATURE = "temperature"
        const val KEY_HUMIDITY = "humidity"
        const val KEY_RAIN = "rain"
        const val KEY_SUN = "sun"
        const val KEY_RECOMMENDATION = "recommendation"
    }
}
