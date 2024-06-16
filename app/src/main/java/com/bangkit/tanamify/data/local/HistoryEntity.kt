package com.bangkit.tanamify.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "result")
    val result: String,
    @ColumnInfo(name = "temperature")
    val temperature: Float,
    @ColumnInfo(name = "humidity")
    val humidity: Float,
    @ColumnInfo(name = "rain")
    val rain: Float,
    @ColumnInfo(name = "sun")
    val sun: Float,
    @ColumnInfo(name = "recommendation")
    val recommendation: String,
    @ColumnInfo(name = "uri")
    val uri: String
)
