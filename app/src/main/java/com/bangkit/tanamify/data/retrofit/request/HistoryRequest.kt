package com.bangkit.tanamify.data.retrofit.request

data class HistoryRequest(
    val id: String,
    val result: String,
    val soil: String,
    val temp: Float,
    val humidity: Float,
    val rain: Float,
    val sun: Float,
    val createdAt: String,
    val image: String
)
