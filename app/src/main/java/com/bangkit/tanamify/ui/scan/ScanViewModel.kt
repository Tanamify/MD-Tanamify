package com.bangkit.tanamify.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Ini adalah fragment analisis"
    }
    val text: LiveData<String> = _text
}