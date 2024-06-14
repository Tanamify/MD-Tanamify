package com.bangkit.tanamify.ui.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.tanamify.MainActivity
import com.bangkit.tanamify.databinding.ActivityResultBinding
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var tflite: Interpreter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        try {
            tflite = Interpreter(loadModelFile("model_ann_new.tflite"))
        } catch (e: Exception) {
            showToast("Failed to load TFLite model: ${e.message}")
        }

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val soilClassification = intent.getStringExtra(EXTRA_SOIL_CLASSIFICATION)
        val inputArray = intent.getFloatArrayExtra(EXTRA_INPUT_ARRAY)

        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

        soilClassification?.let {
            Log.d("Soil Classification", "showSoilClassification: $it")
            binding.tvResultText.text = it
        }

        inputArray?.let {
            binding.tvResultTemp.text = "${it[0]} °C"
            binding.tvResultHumidity.text = "${it[1]} %"
            binding.tvResultRain.text = "${it[2]} mm"
            binding.tvResultSun.text = "${it[3]} hours"
            showRecommendation(it)
        }

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
    }

    private fun runAnnModel(inputArray: FloatArray): String {
        if (tflite == null) {
            showToast("TFLite interpreter is not initialized")
            return "Unknown"
        }

        return try {
            val input = arrayOf(inputArray)
            val output = Array(1) { FloatArray(NUM_OUTPUT_CLASSES) }

            tflite!!.run(input, output)

            val recommendation = getRecommendationLabel(output[0])

            recommendation
        } catch (e: Exception) {
            showToast("Failed to run ANN model: ${e.message}")
            "Unknown"
        }
    }

    private fun getRecommendationLabel(outputArray: FloatArray): String {
        val maxIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1
        return when (maxIndex) {
            0 -> "Kedelai"
            1 -> "Jagung"
            2 -> "Ubi Jalar"
            3 -> "Ubi Kayu"
            4 -> "Padi Gogo"
            5 -> "Padi Sawah"
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

    override fun onDestroy() {
        tflite?.close()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_SOIL_CLASSIFICATION = "extra_soil_classification"
        const val EXTRA_INPUT_ARRAY = "extra_input_array"
        const val NUM_OUTPUT_CLASSES = 6 // Number of plant classes in the model
    }
}
