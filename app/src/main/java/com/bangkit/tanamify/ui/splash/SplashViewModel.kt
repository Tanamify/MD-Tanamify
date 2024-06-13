package com.bangkit.tanamify.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.tanamify.data.pref.UserModel
import com.bangkit.tanamify.repository.HistoryRepository


class SplashViewModel(private val repository: HistoryRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}