package com.dicoding.mystoryapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.network.response.ListStoryItem
import com.dicoding.mystoryapp.network.response.StoryResponse
import com.dicoding.mystoryapp.network.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel() {
    private val _userLocation = MutableLiveData<List<ListStoryItem>>()
    val userLocation : LiveData<List<ListStoryItem>> = _userLocation

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun showLocations(token: String) {
        _isLoading.value = true
        val client =  ApiConfig.getApiService(token).getStoryWithLocation()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _userLocation.value = responseBody?.listStory

                } else {
                    Log.e(TAG, "onFailure: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }
}