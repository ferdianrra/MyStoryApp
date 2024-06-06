package com.dicoding.mystoryapp.view.AddStory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.network.response.AddStoryResponse
import com.dicoding.mystoryapp.network.retrofit.ApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel: ViewModel() {

    private var myLat: RequestBody? = null
    private var myLon: RequestBody? = null
    private val _errorNotif = MutableLiveData<AddStoryResponse?>()
    val errorNotif : LiveData<AddStoryResponse?> = _errorNotif

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun addStory(token: String, desc: RequestBody, photo: MultipartBody.Part, lat:Double? = null, lon:Double? = null) {
        _isLoading.value = true
        if(lat!=null && lon != null) {
            myLat = lat.toString().toRequestBody("text/plain".toMediaType())
            myLon = lon.toString().toRequestBody("text/plain".toMediaType())
        }
        val client = ApiConfig.getApiService(token).addStory(photo, desc, myLat, myLon)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                _errorNotif.value = responseBody

                if (!response.isSuccessful ) {
                    Log.e(TAG, "onFailure ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _isLoading.value = false
               Log.e(TAG, "onFailure ${t.message.toString()}")
            }

        })
    }

    companion object {
        private const val TAG = "AddStoryViewModel"
    }
}