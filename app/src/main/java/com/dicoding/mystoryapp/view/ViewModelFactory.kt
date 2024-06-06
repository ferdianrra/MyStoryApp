package com.dicoding.mystoryapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystoryapp.data.FeedsRepository
import com.dicoding.mystoryapp.network.UserRepository
import com.dicoding.mystoryapp.di.Injection
import com.dicoding.mystoryapp.view.main.MainViewModel
import com.dicoding.mystoryapp.view.signin.SigninViewModel

class ViewModelFactory(private val repository: UserRepository, private val feedsRepository: FeedsRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository, feedsRepository) as T
            }
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideRepository(context),
                        Injection.provideFeedsRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}