package com.bangkit.tanamify.repository

import android.app.Application
import com.bangkit.tanamify.data.api.ApiService
import com.bangkit.tanamify.data.local.HistoryEntity
import com.bangkit.tanamify.data.local.TanamifyDatabase
import com.bangkit.tanamify.data.pref.UserModel
import com.bangkit.tanamify.data.pref.UserPreference
import com.bangkit.tanamify.data.retrofit.response.LoginRequest
import com.bangkit.tanamify.data.retrofit.response.LoginResponse
import com.bangkit.tanamify.data.retrofit.response.RegisterRequest
import com.bangkit.tanamify.data.retrofit.response.RegisterResponse
import com.bangkit.tanamify.data.state.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HistoryRepository(
    private val userPreference: UserPreference,
    private var apiService: ApiService,
    application: Application
) {
    private val historyDao = TanamifyDatabase.getDatabase(application).historyDao()

    suspend fun addHistory(historyEntity: HistoryEntity) {
        historyDao.addHistory(historyEntity)
    }

    fun getHistory(): List<HistoryEntity> = historyDao.getHistory()
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun userLogin(email: String, password: String): Flow<ResultState<LoginResponse>> = flow {
        try {
            val response = apiService.userLogin(LoginRequest(email, password))
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<ResultState<RegisterResponse>> = flow {
        try {
            val response = apiService.userRegister(RegisterRequest(name, email, password))
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)


    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun updateApiService(apiService: ApiService) {
        this.apiService = apiService
    }

    companion object {
        @Volatile
        private var instance: HistoryRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
            application: Application
        ): HistoryRepository =
            instance ?: synchronized(this) {
                instance ?: HistoryRepository(
                    userPreference,
                    apiService,
                    application
                ).also { instance = it }
            }
    }
}