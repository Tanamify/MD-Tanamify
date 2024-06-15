package com.bangkit.tanamify.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
)
