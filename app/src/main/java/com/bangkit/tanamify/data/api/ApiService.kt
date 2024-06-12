package com.bangkit.tanamify.data.api

import com.bangkit.tanamify.data.retrofit.response.LoginRequest
import com.bangkit.tanamify.data.retrofit.response.LoginResponse
import com.bangkit.tanamify.data.retrofit.response.RegisterRequest
import com.bangkit.tanamify.data.retrofit.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun userLogin(@Body loginRequest: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun userRegister(@Body registerRequest: RegisterRequest): RegisterResponse
}
