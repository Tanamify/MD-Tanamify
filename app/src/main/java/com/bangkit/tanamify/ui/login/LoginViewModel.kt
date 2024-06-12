package com.bangkit.tanamify.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.tanamify.data.pref.UserModel
import com.bangkit.tanamify.data.retrofit.response.LoginResponse
import com.bangkit.tanamify.data.state.ResultState
import com.bangkit.tanamify.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: HistoryRepository) : ViewModel() {

    fun userLogin(email: String, password: String): Flow<ResultState<LoginResponse>> {
        return repository.userLogin(email, password)
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}