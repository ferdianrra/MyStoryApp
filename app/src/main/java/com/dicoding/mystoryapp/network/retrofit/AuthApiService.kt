package com.dicoding.mystoryapp.network.retrofit

import com.dicoding.mystoryapp.network.response.LoginResponse
import com.dicoding.mystoryapp.network.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {
    @FormUrlEncoded
    @POST("register")
    fun registAccount(
        @Field("name") name:String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginAccount(
        @Field("email") email:String,
        @Field("password") password: String
    ): Call<LoginResponse>
}