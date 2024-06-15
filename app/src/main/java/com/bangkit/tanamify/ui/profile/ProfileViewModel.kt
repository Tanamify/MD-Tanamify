package com.bangkit.tanamify.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.tanamify.repository.HistoryRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: HistoryRepository,
) : ViewModel() {

    private val _logoutState = MutableLiveData<String>()
    val logoutState: LiveData<String> = _logoutState

    private val _text = MutableLiveData<String>().apply {
        value = "Name"
    }

    val text: LiveData<String> = _text

    fun logout() {
        viewModelScope.launch {
            try {
                repository.logout()
                _logoutState.postValue("Logout successful")
            } catch (e: Exception) {
                _logoutState.postValue("Logout failed: ${e.message}")
            }
        }
    }

}