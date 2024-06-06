package com.dicoding.mystoryapp.view.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystoryapp.network.UserRepository
import com.dicoding.mystoryapp.network.pref.UserModel
import com.dicoding.mystoryapp.network.response.LoginResponse
import com.dicoding.mystoryapp.network.response.LoginResult
import com.dicoding.mystoryapp.network.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SigninViewModel (private val repository: UserRepository): ViewModel() {

    private val _resultAccount = MutableLiveData<LoginResult>()
    val resultAccount: LiveData<LoginResult> = _resultAccount

    private val _message = MutableLiveData<String>()
    val message : LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun loginAccount(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getAuthApiService().loginAccount(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _resultAccount.value = responseBody?.loginResult
                } else {
                    val error = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(error, LoginResponse::class.java)
                    _message.value = errorResponse.message
                    Log.e(TAG, "onFailure: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    companion object {
        private const val TAG = "SigninViewModel"
    }
}