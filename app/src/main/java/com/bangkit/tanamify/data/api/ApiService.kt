package com.bangkit.tanamify.data.api

import com.bangkit.tanamify.data.retrofit.request.HistoryRequest
import com.bangkit.tanamify.data.retrofit.request.LoginRequest
import com.bangkit.tanamify.data.retrofit.request.RegisterRequest
import com.bangkit.tanamify.data.retrofit.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun userLogin(@Body loginRequest: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun userRegister(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): LogoutResponse

    @POST("predict/add")
    suspend fun addHistory(@Header("Authorization") token: String, @Body historyRequest: HistoryRequest): Response<Unit>

    @GET("predict/user-predictions")
    suspend fun getHistories(@Header("Authorization") token: String, @Query("userId") userId: String): PredictionsResponse

    @PUT("predict/delete/{idpred}")
    suspend fun deleteHistory(@Header("Authorization") token: String, @Path("idpred") idpred: String): Response<Unit>
}
