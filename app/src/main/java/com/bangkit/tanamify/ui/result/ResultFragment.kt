package com.bangkit.tanamify.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bangkit.tanamify.R

class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_result, container, false)

        val textViewResult: TextView = rootView.findViewById(R.id.tv_result)

        // Get result from arguments
        val result = arguments?.getFloatArray("result")
        result?.let {
            val resultText = "Result: ${it.contentToString()}"
            textViewResult.text = resultText
        }

        return rootView
    }
}
