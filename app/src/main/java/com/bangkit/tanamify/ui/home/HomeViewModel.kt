package com.bangkit.tanamify.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.tanamify.data.retrofit.response.HistoryResponse
import com.bangkit.tanamify.repository.HistoryRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val historyRepository: HistoryRepository
) : AndroidViewModel(application) {
    private val _historyList: MutableLiveData<List<HistoryResponse>> = MutableLiveData()
    val historyList: LiveData<List<HistoryResponse>> = _historyList

    fun loadHistoryFromServer(token: String, userId: String) {
        viewModelScope.launch {
            try {
                val historyList = historyRepository.fetchHistoryFromServer(token, userId)
                _historyList.value = historyList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteHistoryFromServer(token: String, idpred: String) {
        viewModelScope.launch {
            try {
                val success = historyRepository.deleteHistoryFromServer(token, idpred)
                if (success) {
                    _historyList.value = _historyList.value?.filter { it.idpred != idpred }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



