package com.dicoding.mystoryapp.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.network.response.RegisterResponse
import com.dicoding.mystoryapp.network.retrofit.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel: ViewModel() {

    private var _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message

    private var _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun createAccount(name: String, email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getAuthApiService().registAccount(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse (
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    val error = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(error, RegisterResponse::class.java)
                    _isError.value = true
                    _message.value  = errorResponse.message
                    Log.e(TAG, "onFailures: ${response.errorBody()?.string()}")
                } else {
                    _isError.value = false
                }
            }


            override fun onFailure(call: Call<RegisterResponse>, t:Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    companion object {
        private const val TAG = "RegisterViewModel"
    }
}