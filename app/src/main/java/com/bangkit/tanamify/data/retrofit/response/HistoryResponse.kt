package com.bangkit.tanamify.data.retrofit.response

data class HistoryResponse(
    val id: String,
    val idpred: String,
    val result: String,
    val soil: String,
    val temp: Float,
    val humidity: Float,
    val rain: Float,
    val sun: Float,
    val createdAt: String,
    val image: String
)