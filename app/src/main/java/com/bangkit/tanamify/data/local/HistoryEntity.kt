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
    @ColumnInfo(name = "recommendation")
    val recommendation: String,
    @ColumnInfo(name = "uri")
    val uri: String
)
