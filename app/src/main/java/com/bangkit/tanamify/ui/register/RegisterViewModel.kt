package com.bangkit.tanamify.ui.register

import androidx.lifecycle.ViewModel
import com.bangkit.tanamify.data.retrofit.response.RegisterResponse
import com.bangkit.tanamify.data.state.ResultState
import com.bangkit.tanamify.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow

class RegisterViewModel(private val repository: HistoryRepository) : ViewModel() {

    fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<ResultState<RegisterResponse>> {
        return repository.userRegister(name, email, password)
    }
}
