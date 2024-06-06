package com.dicoding.mystoryapp.di

import android.content.Context
import android.media.session.MediaSession.Token
import com.dicoding.mystoryapp.data.FeedsRepository
import com.dicoding.mystoryapp.network.UserRepository
import com.dicoding.mystoryapp.network.pref.UserPreferences
import com.dicoding.mystoryapp.network.pref.dataStore
import com.dicoding.mystoryapp.network.retrofit.ApiConfig
import com.dicoding.mystoryapp.network.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideFeedsRepository(context: Context): FeedsRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return FeedsRepository(apiService)
    }

    fun updateToken(token:String): FeedsRepository {
        val apiService = ApiConfig.getApiService(token)
        return FeedsRepository(apiService)
    }
}