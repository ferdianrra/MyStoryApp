package com.dicoding.mystoryapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.mystoryapp.data.FeedsRepository
import com.dicoding.mystoryapp.di.Injection
import com.dicoding.mystoryapp.network.UserRepository
import com.dicoding.mystoryapp.network.pref.UserModel
import com.dicoding.mystoryapp.network.response.ListStoryItem
import com.dicoding.mystoryapp.network.response.StoryResponse
import com.dicoding.mystoryapp.network.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel (val repository: UserRepository, var feedsRepository: FeedsRepository) : ViewModel() {
    private val _feedsPhoto = MutableLiveData<PagingData<ListStoryItem>>()
    val feedsPhoto : LiveData<PagingData<ListStoryItem>> = _feedsPhoto

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        refreshFeeds()
    }

    fun refreshFeeds() {
        _isLoading.value = false
        feedsRepository.getFeeds().cachedIn(viewModelScope).observeForever {
            _feedsPhoto.postValue(it)
            _isLoading.value = false
        }

    }

    fun updateToken(token: String) {
        feedsRepository = Injection.updateToken(token)
        refreshFeeds()
    }

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = false
        return repository.getSession().asLiveData()
    }


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}