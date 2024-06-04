package com.bangkit.tanamify.repository

import android.app.Application
import com.bangkit.tanamify.data.local.AsclepiusDatabase
import com.bangkit.tanamify.data.local.HistoryEntity

class HistoryRepository(application: Application) {
    private val historyDao = AsclepiusDatabase.getDatabase(application).historyDao()

    suspend fun addHistory(historyEntity: HistoryEntity) {
        historyDao.addHistory(historyEntity)
    }

    fun getHistory(): List<HistoryEntity> = historyDao.getHistory()
}