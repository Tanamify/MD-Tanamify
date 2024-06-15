package com.bangkit.tanamify.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.tanamify.data.local.HistoryEntity
import com.bangkit.tanamify.repository.HistoryRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val historyRepository: HistoryRepository
) : AndroidViewModel(application) {
    private val _historyList: MutableLiveData<List<HistoryEntity>> = MutableLiveData()
    val historyList: LiveData<List<HistoryEntity>> = _historyList

    init {
        loadHistory()
    }

    fun addHistory(historyEntity: HistoryEntity) {
        viewModelScope.launch {
            historyRepository.addHistory(historyEntity)
            loadHistory()
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _historyList.value = historyRepository.getHistory()
        }
    }
}