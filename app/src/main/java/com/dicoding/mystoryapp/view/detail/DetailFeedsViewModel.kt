package com.dicoding.mystoryapp.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.network.response.DetailStoryResponse
import com.dicoding.mystoryapp.network.response.Story
import com.dicoding.mystoryapp.network.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFeedsViewModel: ViewModel() {
    private val _detailFeeds = MutableLiveData<Story>()
    val detailFeeds : LiveData<Story> = _detailFeeds

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun showDetailFeeds(id : String, token:String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getDetailStory(id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
               if (response.isSuccessful) {
                   val responseBody = response.body()
                   _detailFeeds.value = responseBody?.story
               } else {
                   Log.e(TAG, "onFailure: ${response.errorBody()?.string()}")
               }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }


        })
    }

    companion object {
        private const val TAG = "DetailFeedsViewModel"
    }
}