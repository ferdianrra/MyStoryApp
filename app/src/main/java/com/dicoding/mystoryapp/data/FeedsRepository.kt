package com.dicoding.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.mystoryapp.network.response.ListStoryItem
import com.dicoding.mystoryapp.network.retrofit.ApiService

class FeedsRepository (private val apiService: ApiService) {

    fun getFeeds(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                FeedsPagingSource(apiService)
            }
        ).liveData
    }
}