package com.bangkit.tanamify.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("userId")
    val userId: String
)
