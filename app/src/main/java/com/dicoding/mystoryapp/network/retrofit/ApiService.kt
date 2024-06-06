package com.dicoding.mystoryapp.network.retrofit

import com.dicoding.mystoryapp.network.response.AddStoryResponse
import com.dicoding.mystoryapp.network.response.DetailStoryResponse
import com.dicoding.mystoryapp.network.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("stories")
    suspend fun getStory(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("stories")
    fun getStoryWithLocation(
        @Query("location") location : Int = 1
    ):Call<StoryResponse>

    @GET("stories/{id}")
    fun getDetailStory(@Path("id") id: String):Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ):Call<AddStoryResponse>

}