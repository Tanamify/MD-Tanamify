package com.bangkit.tanamify.data.di

import android.app.Application
import android.content.Context
import com.bangkit.tanamify.data.api.ApiConfig
import com.bangkit.tanamify.data.pref.UserPreference
import com.bangkit.tanamify.data.pref.dataStore
import com.bangkit.tanamify.repository.HistoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object Injection {
    fun provideRepository(context: Context): HistoryRepository {
        val application = context.applicationContext as Application
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return HistoryRepository.getInstance(pref, apiService, application)

    }

    fun provideApplication(context: Context): Application {
        return context.applicationContext as Application
    }

}